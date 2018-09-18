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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.AliasQuery;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.util.StreamUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.existsQuery;
import static org.elasticsearch.index.query.QueryBuilders.geoBoundingBoxQuery;
import static org.elasticsearch.index.query.QueryBuilders.geoDistanceQuery;
import static org.elasticsearch.index.query.QueryBuilders.idsQuery;
import static org.elasticsearch.index.query.QueryBuilders.nestedQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

public class ElasticsearchOrganisationRepository implements OrganisationRepository {

    private static final int DEFAULT_MAX_RESULT_SIZE = 10000;

    private final ElasticsearchConfiguration elasticsearchConfiguration;
    private final ElasticsearchTemplate elasticsearchTemplate;
    private final String indexName;

    public ElasticsearchOrganisationRepository(ElasticsearchConfiguration elasticsearchConfiguration,
                                               ElasticsearchTemplate elasticsearchTemplate,
                                               String indexName) {
        this.elasticsearchConfiguration = elasticsearchConfiguration;
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.indexName = indexName;
    }

    @Override
    public boolean existsOrganisationWithSameTypeInDistance(Organisation organisation, Long distanceInMeters) {
        Address address = organisation.getDefaultAddress();
        if (address == null) {
            return false;
        }
        BoolQueryBuilder organisationListQuery = buildQueryForOrganisationWithSameTypeInDistance(organisation, distanceInMeters);
        NativeSearchQuery query = new NativeSearchQuery(organisationListQuery);
        query.addIndices(indexName);
        query.addTypes(elasticsearchConfiguration.getType().getOrganisation());
        return this.elasticsearchTemplate.count(query) > 0;
    }

    @Override
    public Organisation findOrganisationWithSameTypeInDistance(Organisation organisation, Long distanceInMeters) {
        BoolQueryBuilder nativeSearchQuery = buildQueryForOrganisationWithSameTypeInDistance(organisation, distanceInMeters);
        return search(nativeSearchQuery)
            .findFirst()
            .orElse(null);
    }

    @Override
    public Organisation findByUrlName(String urlName) {
        return search(termQuery("urlName", urlName))
            .findFirst()
            .orElse(null);
    }


    @Override
    public Organisation findOne(String id) {
        return search(idsQuery(this.elasticsearchConfiguration.getType().getOrganisation()).ids(id))
            .findFirst()
            .orElse(null);
    }

    @Override
    public List<ScoredOrganisation> findOrganisationsByQuestionAnswersAndDistanceSortByAnswerMatchAndDistance(
        List<QuestionAnswer> questionAnswers, GeoPoint position, double distance
    ) {
        return findOrganisationsWithQuestionsAndFilter(
            questionAnswers,
            filterDistance(position, distance)
        )
            .sorted((organisation1, organisation2) -> compareOrganisationsBasedOnScoreAndDistance(organisation1, organisation2, position))
            .collect(Collectors.toList());
    }

    @Override
    public List<Organisation> findOrganisationsByDistanceSortByDistance(GeoPoint position, double distance) {
        return
            search(filterDistance(position, distance))
                .sorted((organisation1, organisation2) -> compareOrganisationsBasedOnDistance(organisation1, organisation2, position))
                .collect(Collectors.toList());
    }

    @Override
    public List<ScoredOrganisation> findGlobalOrganisationsByQuestionAnswersSortByAnswerMatch(
        List<QuestionAnswer> questionAnswers
    ) {
        return findOrganisationsWithQuestionsAndFilter(
            questionAnswers,
            boolQuery()
                .mustNot(existsQuery("defaultAddress"))
        )
            .sorted((organisation1, organisation2) -> compareOrganisationsBasedOnScoreAndDistance(organisation1, organisation2, null))
            .collect(Collectors.toList());
    }

    @Override
    public List<Organisation> findGlobalOrganisations() {
        return
            search(
                boolQuery()
                    .mustNot(existsQuery("defaultAddress"))
            )
                .sorted(Comparator.comparing(Organisation::getName))
                .collect(Collectors.toList());
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

        Stream<Organisation> organisations = search(boolQueryBuilder);
        return extractOrganisations(Collections.emptyList(), organisations)
            .map(ScoredOrganisation::getOrganisation)
            .map(Organisation::getDefaultAddress)
            .filter(Objects::nonNull)
            .map(Address::getLocation)
            .collect(Collectors.toList());
    }

    @Override
    public void save(List<? extends Organisation> items) {
        List<IndexQuery> indexQueries = items.stream()
            .map(item -> new IndexQueryBuilder()
                .withId(item.getId().getValue())
                .withObject(item))
            .map(builder -> builder.withType(this.elasticsearchConfiguration.getType().getOrganisation()))
            .map(builder -> builder.withIndexName(indexName))
            .map(IndexQueryBuilder::build)
            .collect(Collectors.toList());

        this.elasticsearchTemplate.bulkIndex(indexQueries);
    }

    @Override
    public void createIndex(String mapping) {
        if (!this.elasticsearchTemplate.indexExists(indexName)) {
            this.elasticsearchTemplate.createIndex(indexName);
            this.elasticsearchTemplate.putMapping(indexName, this.elasticsearchConfiguration.getType().getOrganisation(), mapping);
        }
    }

    @Override
    public void deleteIndex() {
        this.elasticsearchTemplate.deleteIndex(indexName);
    }

    @Override
    public void updateAlias(String alias) {
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
        aliasQuery.setIndexName(indexName);
        elasticsearchTemplate.addAlias(aliasQuery);
    }

    private BoolQueryBuilder buildQueryForOrganisationWithSameTypeInDistance(Organisation organisation, Long distanceInMeters) {
        BoolQueryBuilder organisationListQuery = boolQuery();
        organisationListQuery.must(termQuery("organisationType", organisation.getOrganisationType().name()));

        Address address = organisation.getDefaultAddress();
        if (address == null) {
            organisationListQuery.mustNot(existsQuery("defaultAddress"));
        } else {
            GeoPoint locationToCheck = address.getLocation();

            GeoDistanceQueryBuilder geoDistanceQuery = geoDistanceQuery("defaultAddress.location")
                .point(locationToCheck.getLat(), locationToCheck.getLon())
                .distance(distanceInMeters, DistanceUnit.METERS);
            organisationListQuery.must(geoDistanceQuery);
        }
        return organisationListQuery;
    }

    private Stream<ScoredOrganisation> findOrganisationsWithQuestionsAndFilter(List<QuestionAnswer> questionAnswers,
                                                                               QueryBuilder filter) {
        BoolQueryBuilder boolQueryBuilder = boolQuery();
        questionAnswers
            .stream()
            .filter(questionAnswer -> Objects.nonNull(questionAnswer.getAnswer()))
            .map(this::buildQuestionQuery)
            .forEach(boolQueryBuilder::should);
        boolQueryBuilder.filter(filter);

        return extractOrganisations(
            questionAnswers,
            search(boolQueryBuilder)
        );
    }

    private int compareOrganisationsBasedOnScoreAndDistance(ScoredOrganisation organisation1, ScoredOrganisation organisation2, GeoPoint position) {
        float score1 = organisation1.getScore();
        float score2 = organisation2.getScore();

        if (score1 < score2) {
            return 1;
        } else if (score1 > score2) {
            return -1;
        } else if (position != null) {
            return compareOrganisationsBasedOnDistance(organisation1.getOrganisation(), organisation2.getOrganisation(), position);
        } else {
            return 1;
        }
    }

    private int compareOrganisationsBasedOnDistance(Organisation organisation1, Organisation organisation2, GeoPoint position) {
        double distance1 = GeoPoint.distanceInKm(organisation1.getDefaultAddress().getLocation(), position);
        double distance2 = GeoPoint.distanceInKm(organisation2.getDefaultAddress().getLocation(), position);
        return distance1 > distance2 ? 1 : -1;
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
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);
                if (userAnswer == null) {
                    return null;
                }

                if (Objects.equals(organisationAnswer, userAnswer)) {
                    return 100;
                } else if (organisationAnswer.getNeighbours().contains(userAnswer)) {
                    return 50;
                }
                return 0;
            })
            .filter(Objects::nonNull)
            .collect(Collectors.averagingDouble(value -> value))
            .floatValue();
    }

    private Stream<Organisation> search(QueryBuilder query) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(query);
        nativeSearchQuery.addIndices(indexName);
        nativeSearchQuery.addTypes(elasticsearchConfiguration.getType().getOrganisation());
        nativeSearchQuery.setPageable(new PageRequest(0, DEFAULT_MAX_RESULT_SIZE));
        return StreamUtils.createStreamFromIterator(
            this.elasticsearchTemplate.stream(nativeSearchQuery, Organisation.class)
        );
    }

}
