package de.helfenkannjeder.helfomat.infrastructure.elasticsearch.organisation;

import de.helfenkannjeder.helfomat.core.geopoint.BoundingBox;
import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint;
import de.helfenkannjeder.helfomat.core.organisation.Address;
import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationRepository;
import de.helfenkannjeder.helfomat.core.organisation.ScoredOrganisation;
import de.helfenkannjeder.helfomat.core.question.Answer;
import de.helfenkannjeder.helfomat.core.question.Question;
import de.helfenkannjeder.helfomat.infrastructure.elasticsearch.ElasticsearchConfiguration;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.GeoBoundingBoxQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.rest.action.admin.indices.alias.delete.AliasesNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.AliasQuery;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.existsQuery;
import static org.elasticsearch.index.query.QueryBuilders.geoBoundingBoxQuery;
import static org.elasticsearch.index.query.QueryBuilders.geoDistanceQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.nestedQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

@Component
public class ElasticsearchOrganisationRepository implements OrganisationRepository {

    private static final int DEFAULT_MAX_RESULT_SIZE = 10000;

    private final ElasticsearchCrudOrganisationRepository elasticsearchCrudOrganisationRepository;
    private final ElasticsearchConfiguration elasticsearchConfiguration;
    private final ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    public ElasticsearchOrganisationRepository(ElasticsearchCrudOrganisationRepository elasticsearchCrudOrganisationRepository,
                                               ElasticsearchConfiguration elasticsearchConfiguration, ElasticsearchTemplate elasticsearchTemplate) {
        this.elasticsearchCrudOrganisationRepository = elasticsearchCrudOrganisationRepository;
        this.elasticsearchConfiguration = elasticsearchConfiguration;
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    public boolean existsOrganisationWithSameTypeInDistance(String index, Organisation organisation, Long distanceInMeters) {
        Address address = organisation.getDefaultAddress();
        if (address == null) {
            return false;
        }
        GeoPoint locationToCheck = address.getLocation();

        GeoDistanceQueryBuilder geoDistanceQuery = geoDistanceQuery("defaultAddress.location")
            .point(locationToCheck.getLat(), locationToCheck.getLon())
            .distance(distanceInMeters, DistanceUnit.METERS);

        BoolQueryBuilder organisationListQuery = boolQuery()
            .must(termQuery("organisationType", organisation.getOrganisationType().name()))
            .must(geoDistanceQuery);
        NativeSearchQuery query = new NativeSearchQuery(organisationListQuery);
        query.addIndices(index);
        query.addTypes(elasticsearchConfiguration.getType().getOrganisation());
        return this.elasticsearchTemplate.count(query) > 0;
    }

    @Override
    public Organisation findOne(String id) {
        return this.elasticsearchCrudOrganisationRepository.findOne(id);
    }

    @Override
    public List<ScoredOrganisation> findOrganisations(Map<String, Answer> questionAnswers,
                                                      GeoPoint position,
                                                      double distance) {
        return findOrganisationsWithQuestionsAndFilterAndSort(
            questionAnswers,
            filterDistance(position, distance)
        );
    }

    @Override
    public List<ScoredOrganisation> findGlobalOrganisations(Map<String, Answer> questionAnswers) {
        return findOrganisationsWithQuestionsAndFilterAndSort(
            questionAnswers,
            boolQuery()
                .mustNot(existsQuery("defaultAddress"))
        );
    }

    private List<ScoredOrganisation> findOrganisationsWithQuestionsAndFilterAndSort(Map<String, Answer> questionAnswers,
                                                                                    QueryBuilder filter) {
        BoolQueryBuilder boolQueryBuilder = boolQuery();
        for (Map.Entry<String, Answer> questionAnswerDto : questionAnswers.entrySet()) {
            boolQueryBuilder.should(buildQuestionQuery(questionAnswerDto));
        }

        boolQueryBuilder.filter(filter);

        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(boolQueryBuilder);
        nativeSearchQuery.addIndices(elasticsearchConfiguration.getIndex());
        nativeSearchQuery.addTypes(elasticsearchConfiguration.getType().getOrganisation());
        nativeSearchQuery.setPageable(new PageRequest(0, DEFAULT_MAX_RESULT_SIZE));
        List<Organisation> organisations = this.elasticsearchTemplate.queryForList(nativeSearchQuery, Organisation.class);

        return extractOrganisations(
            questionAnswers,
            organisations
        );
    }

    @Override
    public List<GeoPoint> findClusteredGeoPoints(GeoPoint position,
                                                 double distance,
                                                 BoundingBox boundingBox) {
        BoolQueryBuilder boolQueryBuilder = boolQuery();
        BoolQueryBuilder positionQuery = boolQuery()
            .must(filterBox(boundingBox));

        GeoDistanceQueryBuilder distanceFilterToPosition = filterDistance(position, distance);
        if (distanceFilterToPosition != null) {
            positionQuery
                .mustNot(distanceFilterToPosition);
        }

        boolQueryBuilder.filter(positionQuery);

        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(boolQueryBuilder, matchAllQuery());
        nativeSearchQuery.addIndices(elasticsearchConfiguration.getIndex());
        nativeSearchQuery.addTypes(elasticsearchConfiguration.getType().getOrganisation());
        nativeSearchQuery.setPageable(new PageRequest(0, DEFAULT_MAX_RESULT_SIZE));
        List<Organisation> organisations = this.elasticsearchTemplate.queryForList(nativeSearchQuery, Organisation.class);

        return extractOrganisations(Collections.emptyMap(), organisations)
            .stream()
            .map(ScoredOrganisation::getOrganisation)
            .map(Organisation::getDefaultAddress)
            .filter(Objects::nonNull)
            .map(Address::getLocation)
            .collect(Collectors.toList());
    }

    @Override
    public void save(String index, List<? extends Organisation> items) {
        List<IndexQuery> indexQueries = items.stream()
            .map(item -> new IndexQueryBuilder()
                .withId(String.valueOf(item.getId()))
                .withObject(item))
            .map(builder -> builder.withType(this.elasticsearchConfiguration.getType().getOrganisation()))
            .map(builder -> builder.withIndexName(index))
            .map(IndexQueryBuilder::build)
            .collect(Collectors.toList());

        this.elasticsearchTemplate.bulkIndex(indexQueries);
    }

    @Override
    public void createIndex(String index, String mapping) {
        this.elasticsearchTemplate.createIndex(index);
        this.elasticsearchTemplate.putMapping(index, this.elasticsearchConfiguration.getType().getOrganisation(), mapping);
    }

    @Override
    public void deleteIndex(String index) {
        this.elasticsearchTemplate.deleteIndex(index);
    }

    @Override
    public void updateAlias(String index) {
        String alias = this.elasticsearchConfiguration.getIndex();
        try {
            AliasQuery removeAliasQuery = new AliasQuery();
            removeAliasQuery.setAliasName(alias);
            removeAliasQuery.setIndexName(alias + "-*");
            elasticsearchTemplate.removeAlias(removeAliasQuery);
        } catch (AliasesNotFoundException exception) {
            // Ignore
        }

        AliasQuery aliasQuery = new AliasQuery();
        aliasQuery.setAliasName(alias);
        aliasQuery.setIndexName(index);
        elasticsearchTemplate.addAlias(aliasQuery);
    }

    private QueryBuilder buildQuestionQuery(Map.Entry<String, Answer> questionAnswerDto) {
        BoolQueryBuilder questionQuery = boolQuery()
            .minimumNumberShouldMatch(1)
            .must(termQuery("questions.uid", questionAnswerDto.getKey()))
            .should(termQuery("questions.answer", questionAnswerDto.getValue().toString()).boost(2.0f));

        for (Answer neighbour : questionAnswerDto.getValue().getNeighbours()) {
            questionQuery.should(termQuery("questions.answer", neighbour.toString()).boost(1.0f));
        }

        return nestedQuery("questions", questionQuery);
    }

    private GeoBoundingBoxQueryBuilder filterBox(BoundingBox boundingBoxDto) {
        GeoPoint topRight = boundingBoxDto.getNorthEast();
        GeoPoint bottomLeft = boundingBoxDto.getSouthWest();

        return geoBoundingBoxQuery("defaultAddress.location")
            .topRight(topRight.getLat(), topRight.getLon())
            .bottomLeft(bottomLeft.getLat(), bottomLeft.getLon());
    }

    private GeoDistanceQueryBuilder filterDistance(GeoPoint position, double distance) {
        if (position == null) {
            return null;
        }
        return geoDistanceQuery("defaultAddress.location")
            .lat(position.getLat())
            .lon(position.getLon())
            .distance(distance, DistanceUnit.KILOMETERS);
    }

    private List<ScoredOrganisation> extractOrganisations(Map<String, Answer> questionAnswerList, List<Organisation> resultOrganisations) {
        return resultOrganisations
            .stream()
            .map(organisation -> new ScoredOrganisation(
                organisation,
                calculateScore(organisation.getQuestions(), questionAnswerList)
            ))
            .sorted((o1, o2) -> o1.getScore() < o2.getScore() ? 1 : -1)
            .collect(Collectors.toList());
    }

    private float calculateScore(List<Question> organisationQuestions, Map<String, Answer> questionAnswerList) {
        return organisationQuestions
            .stream()
            .map((question) -> {
                String questionId = String.valueOf(question.getUid());
                Answer organisationAnswer = question.getAnswer();
                Answer userAnswer = questionAnswerList.get(questionId);

                if (Objects.equals(organisationAnswer, userAnswer)) {
                    return 100;
                } else if (organisationAnswer.getNeighbours().contains(userAnswer)) {
                    return 50;
                }
                return 0;
            })
            .collect(Collectors.averagingDouble(value -> value))
            .floatValue();
    }

}
