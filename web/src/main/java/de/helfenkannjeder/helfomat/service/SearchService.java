package de.helfenkannjeder.helfomat.service;

import de.helfenkannjeder.helfomat.domain.GeoPoint;
import de.helfenkannjeder.helfomat.dto.AddressDto;
import de.helfenkannjeder.helfomat.dto.BoundingBoxDto;
import de.helfenkannjeder.helfomat.dto.ClusteredGeoPointDto;
import de.helfenkannjeder.helfomat.dto.GeoPointDto;
import de.helfenkannjeder.helfomat.dto.OrganisationDto;
import de.helfenkannjeder.helfomat.dto.QuestionAnswerDto;
import de.helfenkannjeder.helfomat.dto.QuestionDto;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.GeoBoundingBoxQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.geogrid.GeoHashGrid;
import org.elasticsearch.search.aggregations.bucket.nested.InternalNested;
import org.elasticsearch.search.aggregations.bucket.nested.Nested;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.geoBoundingBoxQuery;
import static org.elasticsearch.index.query.QueryBuilders.geoDistanceQuery;
import static org.elasticsearch.index.query.QueryBuilders.nestedQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

/**
 * @author Valentin Zickner
 */
// TODO: Split in multiple search services, one for organisations and one for questions
@Service
public class SearchService {

    private static final int DEFAULT_MAX_RESULT_SIZE = 10000;
    private Client client;
    private String index;
    private String type;

    @Autowired
    public SearchService(Client client,
                         @Value("${elasticsearch.index}") String index,
                         @Value("${elasticsearch.type.organisation}") String type) {
        this.client = client;
        this.index = index;
        this.type = type;
    }

    public List<OrganisationDto> findOrganisation(List<QuestionAnswerDto> questionAnswerDtos,
                                                  GeoPoint position,
                                                  double distance) {
        BoolQueryBuilder boolQueryBuilder = boolQuery();
        for (QuestionAnswerDto questionAnswerDto : questionAnswerDtos) {
            QueryBuilder questionQuery = buildQuestionQuery(questionAnswerDto);
            if (questionQuery != null) {
                boolQueryBuilder.should(questionQuery);
            }
        }

        boolQueryBuilder.filter(nestedQuery("addresses", filterDistance(position, distance)));

        SortBuilder sortBuilder =
                SortBuilders
                        .geoDistanceSort("addresses.location")
                        .setNestedPath("addresses")
                        .point(position.getLat(), position.getLon())
                        .unit(DistanceUnit.KILOMETERS)
                        .order(SortOrder.DESC);

        SearchResponse searchResponse = client
                .prepareSearch(index)
                .setTypes(type)
                .setQuery(boolQueryBuilder)
                .setSize(DEFAULT_MAX_RESULT_SIZE)
                .addSort(SortBuilders.scoreSort())
                .addSort(sortBuilder)
                .execute()
                .actionGet();
        return extractOrganisations(searchResponse);
    }

    public List<ClusteredGeoPointDto> findClusteredGeoPoints(GeoPoint position,
                                                             double distance,
                                                             BoundingBoxDto boundingBoxDto, int zoom) {
        BoolQueryBuilder boolQueryBuilder = boolQuery();
        boolQueryBuilder.filter(nestedQuery("addresses",
                boolQuery()
                        .must(filterBox(boundingBoxDto))
                        .mustNot(filterDistance(position, distance))
                )
        );

        SearchResponse searchResponse = client
                .prepareSearch(index)
                .setTypes(type)
                .setQuery(boolQueryBuilder)
                .setSize(0) // Hide results, only aggregation relevant
                .addAggregation(
                        AggregationBuilders
                                .nested("addresses")
                                .path("addresses")
                                .subAggregation(AggregationBuilders
                                        .geohashGrid("grouped_organizations")
                                        .field("addresses.location")
                                        .precision(zoom / 2)
                                        .size(DEFAULT_MAX_RESULT_SIZE)
                                )
                )
                .get();
        return extractClusteredOrganisations(searchResponse);
    }

    private QueryBuilder buildQuestionQuery(QuestionAnswerDto questionAnswerDto) {
        if (isQuestionAnswered(questionAnswerDto)) {
            return null;
        }

        return nestedQuery("questions",
                boolQuery()
                        .must(termQuery("questions.uid", questionAnswerDto.getId()))
                        .must(termQuery("questions.answer", convertAnswerToString(questionAnswerDto)))
        );
    }

    private static boolean isQuestionAnswered(QuestionAnswerDto questionAnswerDto) {
        return questionAnswerDto.getAnswer() != -1 && questionAnswerDto.getAnswer() != 1;
    }

    private static String convertAnswerToString(QuestionAnswerDto questionAnswerDto) {
        if (questionAnswerDto.getAnswer() == 1) {
            return "YES";
        }
        return "NO";
    }

    private GeoBoundingBoxQueryBuilder filterBox(BoundingBoxDto boundingBoxDto) {
        GeoPointDto topRight = boundingBoxDto.getNorthEast();
        GeoPointDto bottomLeft = boundingBoxDto.getSouthWest();

        return geoBoundingBoxQuery("addresses.location")
                .topRight(topRight.getLat(), topRight.getLon())
                .bottomLeft(bottomLeft.getLat(), bottomLeft.getLon());
    }

    private GeoDistanceQueryBuilder filterDistance(GeoPoint position, double distance) {
        return geoDistanceQuery("addresses.location")
                .lat(position.getLat())
                .lon(position.getLon())
                .distance(distance, DistanceUnit.KILOMETERS);
    }

    private List<ClusteredGeoPointDto> extractClusteredOrganisations(SearchResponse searchResponse) {
        return searchResponse
                .getAggregations().<InternalNested>get("addresses")
                .getAggregations().<GeoHashGrid>get("grouped_organizations")
                .getBuckets().stream()
                .map(bucket -> new ClusteredGeoPointDto(
                        GeoPointDto.fromGeoPoint(
                                GeoPoint.fromGeoPoint((org.elasticsearch.common.geo.GeoPoint) bucket.getKey())
                        ),
                        bucket.getDocCount()
                ))
                .collect(Collectors.toList());
    }

    private List<OrganisationDto> extractOrganisations(SearchResponse searchResponse) {
        List<OrganisationDto> organisations = new ArrayList<>();
        Float maxScore = null;
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            if (maxScore == null) {
                maxScore = hit.getScore();
            }

            OrganisationDto organisationDto = extractOrganisation(hit.getSource());
            organisationDto.setScoreNorm((hit.getScore() * 100) / maxScore);
            organisations.add(organisationDto);
        }
        return organisations;
    }

    @SuppressWarnings("unchecked")
    private OrganisationDto extractOrganisation(Map<String, Object> source) {
        List<Map<String, Object>> addresses = (List<Map<String, Object>>) source.get("addresses");
        return new OrganisationDto(
                (String) source.get("id"),
                (String) source.get("name"),
                (String) source.get("description"),
                (String) source.get("website"),
                (String) source.get("mapPin"),
                addresses.stream().map(this::extractAddress).collect(Collectors.toList()),
                (String) source.get("logo")
        );
    }

    @SuppressWarnings("unchecked")
    private AddressDto extractAddress(Map<String, Object> address) {
        return new AddressDto(
                (String) address.get("street"),
                (String) address.get("addressAppendix"),
                (String) address.get("city"),
                (String) address.get("zipcode"),
                GeoPointDto.fromGeoPoint(extractGeoPoint((Map<String, Object>) address.get("location"))),
                (String) address.get("telephone"),
                (String) address.get("website")
        );
    }

    private GeoPoint extractGeoPoint(Map<String, Object> geoPoint) {
        return new GeoPoint(
                (double) geoPoint.get("lat"),
                (double) geoPoint.get("lon")
        );
    }

    private SearchResponse executeQuery(QueryBuilder queryBuilder, SortBuilder sortBuilder) {
        return client
                .prepareSearch(index)
                .setTypes(type)
                .setQuery(queryBuilder)
                .setSize(DEFAULT_MAX_RESULT_SIZE)
                .addSort(SortBuilders.scoreSort())
                .addSort(sortBuilder)
                .execute()
                .actionGet();
    }

    public List<QuestionDto> findQuestions() {
        Nested nested = client.prepareSearch(index)
                .addAggregation(AggregationBuilders
                        .nested("questions")
                        .path("questions")
                        .subAggregation(
                                AggregationBuilders.terms("question")
                                        .field("questions.question")
                                        .size(DEFAULT_MAX_RESULT_SIZE)
                                        .subAggregation(
                                                AggregationBuilders.terms("id")
                                                        .field("questions.uid")
                                                        .size(1)
                                        )
                                        .subAggregation(
                                                AggregationBuilders.terms("description")
                                                        .field("questions.description")
                                                        .size(1)
                                        )
                                        .subAggregation(
                                                AggregationBuilders.terms("position")
                                                        .field("questions.position")
                                                        .size(1)
                                        )
                        )
                ).get().getAggregations().get("questions");
        StringTerms questions = nested.getAggregations().get("question");
        return questions.getBuckets().stream()
                .map(this::bucketToQuestion)
                .sorted(this::sortQuestions)
                .collect(Collectors.toList());

    }

    private QuestionDto bucketToQuestion(Terms.Bucket s) {
        String id = String.valueOf(this.getSubbucket(s, "id", this::convertIntBucket).orElse(0));
        String question = s.getKeyAsString();
        String description = this.getSubbucket(s, "description", this::convertStringBucket).orElse(null);
        Integer position = this.getSubbucket(s, "position", this::convertIntBucket).orElse(0);
        return new QuestionDto(id, question, description, position);
    }

    private int sortQuestions(QuestionDto q1, QuestionDto q2) {
        return q1.getPosition() < q2.getPosition() ? -1 : 1;
    }

    private Integer convertIntBucket(Terms.Bucket intBucket) {
        return intBucket.getKeyAsNumber().intValue();
    }

    private String convertStringBucket(Terms.Bucket stringBucket) {
        return stringBucket.getKeyAsString();

    }

    private <T extends Terms, R> Optional<R> getSubbucket(Terms.Bucket bucket,
                                                          String aggregationName,
                                                          Function<Terms.Bucket, R> bucketConversion) {
        return bucket.getAggregations().<T>get(aggregationName)
                .getBuckets()
                .stream()
                .map(bucketConversion)
                .findFirst();
    }

}
