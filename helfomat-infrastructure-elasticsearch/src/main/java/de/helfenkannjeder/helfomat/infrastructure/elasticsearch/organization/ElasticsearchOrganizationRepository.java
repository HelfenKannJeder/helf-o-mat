package de.helfenkannjeder.helfomat.infrastructure.elasticsearch.organization;

import de.helfenkannjeder.helfomat.core.geopoint.BoundingBox;
import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint;
import de.helfenkannjeder.helfomat.core.organization.Address;
import de.helfenkannjeder.helfomat.core.organization.Answer;
import de.helfenkannjeder.helfomat.core.organization.Organization;
import de.helfenkannjeder.helfomat.core.organization.OrganizationRepository;
import de.helfenkannjeder.helfomat.core.organization.QuestionAnswer;
import de.helfenkannjeder.helfomat.core.organization.ScoredOrganization;
import de.helfenkannjeder.helfomat.core.question.QuestionId;
import de.helfenkannjeder.helfomat.infrastructure.elasticsearch.ElasticsearchConfiguration;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.GeoBoundingBoxQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.rest.action.admin.indices.AliasesNotFoundException;
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

public class ElasticsearchOrganizationRepository implements OrganizationRepository {

    private static final int DEFAULT_MAX_RESULT_SIZE = 10000;

    private final ElasticsearchConfiguration elasticsearchConfiguration;
    private final ElasticsearchTemplate elasticsearchTemplate;
    private final String indexName;

    public ElasticsearchOrganizationRepository(ElasticsearchConfiguration elasticsearchConfiguration,
                                               ElasticsearchTemplate elasticsearchTemplate,
                                               String indexName) {
        this.elasticsearchConfiguration = elasticsearchConfiguration;
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.indexName = indexName;
    }

    @Override
    public boolean existsOrganizationWithSameTypeInDistance(Organization organization, Long distanceInMeters) {
        Address address = organization.getDefaultAddress();
        if (address == null) {
            return false;
        }
        BoolQueryBuilder organizationListQuery = buildQueryForOrganizationWithSameTypeInDistance(organization, distanceInMeters);
        NativeSearchQuery query = new NativeSearchQuery(organizationListQuery);
        query.addIndices(indexName);
        query.addTypes(elasticsearchConfiguration.getType().getOrganization());
        return this.elasticsearchTemplate.count(query) > 0;
    }

    @Override
    public Organization findOrganizationWithSameTypeInDistance(Organization organization, Long distanceInMeters) {
        BoolQueryBuilder nativeSearchQuery = buildQueryForOrganizationWithSameTypeInDistance(organization, distanceInMeters);
        List<Organization> organizations = search(nativeSearchQuery).collect(Collectors.toList());
        if (organizations.size() == 1) {
            return organizations.get(0);
        } else if (organizations.size() == 0) {
            return null;
        } else {
            return organizations
                .stream()
                .filter(o -> o.getUrlName().equals(organization.getUrlName()))
                .findFirst()
                .orElse(null);
        }
    }

    @Override
    public Organization findByUrlName(String urlName) {
        return search(termQuery("urlName", urlName))
            .findFirst()
            .orElse(null);
    }


    @Override
    public Organization findOne(String id) {
        return search(idsQuery(this.elasticsearchConfiguration.getType().getOrganization()).addIds(id))
            .findFirst()
            .orElse(null);
    }

    @Override
    public List<ScoredOrganization> findOrganizationsByQuestionAnswersAndDistanceSortByAnswerMatchAndDistance(
        List<QuestionAnswer> questionAnswers, GeoPoint position, double distance
    ) {
        return findOrganizationsWithQuestionsAndFilter(
            questionAnswers,
            filterDistance(position, distance)
        )
            .sorted((organization1, organization2) -> compareOrganizationsBasedOnScoreAndDistance(organization1, organization2, position))
            .collect(Collectors.toList());
    }

    @Override
    public List<Organization> findOrganizationsByDistanceSortByDistance(GeoPoint position, double distance) {
        return
            search(filterDistance(position, distance))
                .sorted((organization1, organization2) -> compareOrganizationsBasedOnDistance(organization1, organization2, position))
                .collect(Collectors.toList());
    }

    @Override
    public List<ScoredOrganization> findGlobalOrganizationsByQuestionAnswersSortByAnswerMatch(
        List<QuestionAnswer> questionAnswers
    ) {
        return findOrganizationsWithQuestionsAndFilter(
            questionAnswers,
            boolQuery()
                .mustNot(existsQuery("defaultAddress"))
        )
            .sorted((organization1, organization2) -> compareOrganizationsBasedOnScoreAndDistance(organization1, organization2, null))
            .collect(Collectors.toList());
    }

    @Override
    public List<Organization> findGlobalOrganizations() {
        return
            search(
                boolQuery()
                    .mustNot(existsQuery("defaultAddress"))
            )
                .sorted(Comparator.comparing(Organization::getName))
                .collect(Collectors.toList());
    }

    @Override
    public List<GeoPoint> findGeoPointsOfOrganizationsInsideBoundingBox(
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

        Stream<Organization> organizations = search(boolQueryBuilder);
        return extractOrganizations(Collections.emptyList(), organizations)
            .map(ScoredOrganization::getOrganization)
            .map(Organization::getDefaultAddress)
            .filter(Objects::nonNull)
            .map(Address::getLocation)
            .collect(Collectors.toList());
    }

    @Override
    public void save(List<? extends Organization> organizations) {
        List<IndexQuery> indexQueries = organizations.stream()
            .map(item -> new IndexQueryBuilder()
                .withId(item.getId().getValue())
                .withObject(item))
            .map(builder -> builder.withType(this.elasticsearchConfiguration.getType().getOrganization()))
            .map(builder -> builder.withIndexName(indexName))
            .map(IndexQueryBuilder::build)
            .collect(Collectors.toList());

        this.elasticsearchTemplate.bulkIndex(indexQueries);
    }

    @Override
    public void createIndex(String mapping) {
        if (!this.elasticsearchTemplate.indexExists(indexName)) {
            this.elasticsearchTemplate.createIndex(indexName);
            this.elasticsearchTemplate.putMapping(indexName, this.elasticsearchConfiguration.getType().getOrganization(), mapping);
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

    private BoolQueryBuilder buildQueryForOrganizationWithSameTypeInDistance(Organization organization, Long distanceInMeters) {
        BoolQueryBuilder organizationListQuery = boolQuery();
        organizationListQuery.must(termQuery("organizationType", organization.getOrganizationType().name()));

        Address address = organization.getDefaultAddress();
        if (address == null) {
            organizationListQuery.mustNot(existsQuery("defaultAddress"));
        } else {
            GeoPoint locationToCheck = address.getLocation();

            GeoDistanceQueryBuilder geoDistanceQuery = geoDistanceQuery("defaultAddress.location")
                .point(locationToCheck.getLat(), locationToCheck.getLon())
                .distance(distanceInMeters, DistanceUnit.METERS);
            organizationListQuery.must(geoDistanceQuery);
        }
        return organizationListQuery;
    }

    private Stream<ScoredOrganization> findOrganizationsWithQuestionsAndFilter(List<QuestionAnswer> questionAnswers,
                                                                               QueryBuilder filter) {
        BoolQueryBuilder boolQueryBuilder = boolQuery();
        questionAnswers
            .stream()
            .filter(questionAnswer -> Objects.nonNull(questionAnswer.getAnswer()))
            .map(this::buildQuestionQuery)
            .forEach(boolQueryBuilder::should);
        boolQueryBuilder.filter(filter);

        return extractOrganizations(
            questionAnswers,
            search(boolQueryBuilder)
        );
    }

    private int compareOrganizationsBasedOnScoreAndDistance(ScoredOrganization organization1, ScoredOrganization organization2, GeoPoint position) {
        float score1 = organization1.getScore();
        float score2 = organization2.getScore();

        if (score1 < score2) {
            return 1;
        } else if (score1 > score2) {
            return -1;
        } else if (position != null) {
            return compareOrganizationsBasedOnDistance(organization1.getOrganization(), organization2.getOrganization(), position);
        } else {
            return 1;
        }
    }

    private int compareOrganizationsBasedOnDistance(Organization organization1, Organization organization2, GeoPoint position) {
        double distance1 = GeoPoint.distanceInKm(organization1.getDefaultAddress().getLocation(), position);
        double distance2 = GeoPoint.distanceInKm(organization2.getDefaultAddress().getLocation(), position);
        return distance1 > distance2 ? 1 : -1;
    }

    private QueryBuilder buildQuestionQuery(QuestionAnswer questionAnswer) {
        BoolQueryBuilder questionQuery = boolQuery()
            .minimumShouldMatch(1)
            .must(termQuery("questions.questionId", questionAnswer.getQuestionId().getValue()))
            .should(termQuery("questions.answer", questionAnswer.getAnswer().toString()).boost(2.0f));

        for (Answer neighbour : questionAnswer.getAnswer().getNeighbours()) {
            questionQuery.should(termQuery("questions.answer", neighbour.toString()).boost(1.0f));
        }

        return nestedQuery("questions", questionQuery, ScoreMode.Max);
    }

    private GeoBoundingBoxQueryBuilder filterBox(BoundingBox boundingBoxDto) {
        GeoPoint topRight = boundingBoxDto.getNorthEast();
        GeoPoint bottomLeft = boundingBoxDto.getSouthWest();

        return geoBoundingBoxQuery("defaultAddress.location")
            .setCornersOGC(toElasticsearchGeoPoint(bottomLeft), toElasticsearchGeoPoint(topRight));
    }

    private GeoDistanceQueryBuilder filterDistance(GeoPoint position, double distance) {
        if (position == null) {
            return null;
        }
        return geoDistanceQuery("defaultAddress.location")
            .point(toElasticsearchGeoPoint(position))
            .distance(distance, DistanceUnit.KILOMETERS);
    }

    private static org.elasticsearch.common.geo.GeoPoint toElasticsearchGeoPoint(GeoPoint geoPoint) {
        return new org.elasticsearch.common.geo.GeoPoint(geoPoint.getLat(), geoPoint.getLon()
        );
    }

    private Stream<ScoredOrganization> extractOrganizations(List<QuestionAnswer> questionAnswers, Stream<Organization> resultOrganizations) {
        return resultOrganizations
            .map(organization -> new ScoredOrganization(
                organization,
                calculateScore(organization.getQuestionAnswers(), questionAnswers)
            ));
    }

    private float calculateScore(List<QuestionAnswer> organizationQuestions, List<QuestionAnswer> questionAnswers) {
        return organizationQuestions
            .stream()
            .map((question) -> {
                QuestionId questionId = question.getQuestionId();
                Answer organizationAnswer = question.getAnswer();
                Answer userAnswer = questionAnswers.stream()
                    .filter(questionAnswer -> questionAnswer.getQuestionId().equals(questionId))
                    .map(QuestionAnswer::getAnswer)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);
                if (userAnswer == null) {
                    return null;
                }

                if (Objects.equals(organizationAnswer, userAnswer)) {
                    return 100;
                } else if (organizationAnswer.getNeighbours().contains(userAnswer)) {
                    return 50;
                }
                return 0;
            })
            .filter(Objects::nonNull)
            .collect(Collectors.averagingDouble(value -> value))
            .floatValue();
    }

    private Stream<Organization> search(QueryBuilder query) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(query);
        nativeSearchQuery.addIndices(indexName);
        nativeSearchQuery.addTypes(elasticsearchConfiguration.getType().getOrganization());
        nativeSearchQuery.setPageable(PageRequest.of(0, DEFAULT_MAX_RESULT_SIZE));
        return StreamUtils.createStreamFromIterator(
            this.elasticsearchTemplate.stream(nativeSearchQuery, Organization.class)
        );
    }

}
