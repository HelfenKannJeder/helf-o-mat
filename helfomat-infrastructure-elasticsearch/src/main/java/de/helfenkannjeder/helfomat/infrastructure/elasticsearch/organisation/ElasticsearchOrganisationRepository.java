package de.helfenkannjeder.helfomat.infrastructure.elasticsearch.organisation;

import de.helfenkannjeder.helfomat.core.geopoint.BoundingBox;
import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint;
import de.helfenkannjeder.helfomat.core.organisation.Address;
import de.helfenkannjeder.helfomat.core.organisation.Answer;
import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationRepository;
import de.helfenkannjeder.helfomat.core.organisation.QuestionAnswer;
import de.helfenkannjeder.helfomat.core.organisation.ScoredOrganisation;
import de.helfenkannjeder.helfomat.core.question.QuestionId;
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
import org.springframework.data.util.StreamUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public List<ScoredOrganisation> findOrganisationsByQuestionAnswersAndDistanceSortByAnswerMatchAndDistance(
        List<QuestionAnswer> questionAnswers, GeoPoint position, double distance
    ) {
        return sortOrganisations(
            findOrganisationsWithQuestionsAndFilter(
                questionAnswers,
                filterDistance(position, distance)
            ),
            position
        )
            .collect(Collectors.toList());
    }

    @Override
    public List<ScoredOrganisation> findGlobalOrganisationsByQuestionAnswersSortByAnswerMatch(
        List<QuestionAnswer> questionAnswers
    ) {
        return sortOrganisations(
            findOrganisationsWithQuestionsAndFilter(
                questionAnswers,
                boolQuery()
                    .mustNot(existsQuery("defaultAddress"))
            )
        )
            .collect(Collectors.toList());
    }

    private Stream<ScoredOrganisation> findOrganisationsWithQuestionsAndFilter(List<QuestionAnswer> questionAnswers,
                                                                               QueryBuilder filter) {
        BoolQueryBuilder boolQueryBuilder = boolQuery();
        for (QuestionAnswer questionAnswer : questionAnswers) {
            boolQueryBuilder.should(buildQuestionQuery(questionAnswer));
        }

        boolQueryBuilder.filter(filter);

        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(boolQueryBuilder);
        nativeSearchQuery.addIndices(elasticsearchConfiguration.getIndex());
        nativeSearchQuery.addTypes(elasticsearchConfiguration.getType().getOrganisation());
        nativeSearchQuery.setPageable(new PageRequest(0, DEFAULT_MAX_RESULT_SIZE));
        Stream<Organisation> organisations = StreamUtils.createStreamFromIterator(
            this.elasticsearchTemplate.stream(nativeSearchQuery, Organisation.class)
        );

        return extractOrganisations(
            questionAnswers,
            organisations
        );
    }

    @Override
    public List<GeoPoint> findGeoPointsOfOrganisationsInsideBoundingBox(
        GeoPoint position, double distance, BoundingBox boundingBox
    ) {
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
        Stream<Organisation> organisations = StreamUtils.createStreamFromIterator(
            this.elasticsearchTemplate.stream(nativeSearchQuery, Organisation.class)
        );

        return extractOrganisations(Collections.emptyList(), organisations)
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

    private Stream<ScoredOrganisation> sortOrganisations(Stream<ScoredOrganisation> organisations) {
        return sortOrganisations(organisations, null);
    }

    private Stream<ScoredOrganisation> sortOrganisations(Stream<ScoredOrganisation> organisations, GeoPoint position) {
        return organisations
            .sorted((organisation1, organisation2) -> {
                float score1 = organisation1.getScore();
                float score2 = organisation2.getScore();

                if (score1 < score2) {
                    return 1;
                } else if (score1 > score2) {
                    return -1;
                } else if (position != null) {
                    double distance1 = GeoPoint.distanceInKm(organisation1.getOrganisation().getDefaultAddress().getLocation(), position);
                    double distance2 = GeoPoint.distanceInKm(organisation2.getOrganisation().getDefaultAddress().getLocation(), position);
                    return distance1 > distance2 ? 1 : -1;
                } else {
                    return 1;
                }
            });
    }

    private QueryBuilder buildQuestionQuery(QuestionAnswer questionAnswer) {
        BoolQueryBuilder questionQuery = boolQuery()
            .minimumNumberShouldMatch(1)
            .must(termQuery("questions.questionId", questionAnswer.getQuestionId().getValue()))
            .should(termQuery("questions.answer", questionAnswer.getAnswer().toString()).boost(2.0f));

        for (Answer neighbour : questionAnswer.getAnswer().getNeighbours()) {
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

    private Stream<ScoredOrganisation> extractOrganisations(List<QuestionAnswer> questionAnswers, Stream<Organisation> resultOrganisations) {
        return resultOrganisations
            .map(organisation -> new ScoredOrganisation(
                organisation,
                calculateScore(organisation.getQuestionAnswers(), questionAnswers)
            ));
    }

    private float calculateScore(List<QuestionAnswer> organisationQuestions, List<QuestionAnswer> questionAnswers) {
        return organisationQuestions
            .stream()
            .map((question) -> {
                QuestionId questionId = question.getQuestionId();
                Answer organisationAnswer = question.getAnswer();
                Answer userAnswer = questionAnswers.stream()
                    .filter(questionAnswer -> questionAnswer.getQuestionId().equals(questionId))
                    .map(QuestionAnswer::getAnswer)
                    .findFirst()
                    .orElse(null);

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
