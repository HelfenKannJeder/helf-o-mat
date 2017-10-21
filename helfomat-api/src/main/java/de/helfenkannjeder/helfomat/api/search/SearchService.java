package de.helfenkannjeder.helfomat.api.search;

import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.GeoBoundingBoxQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
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
import java.util.Collections;
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
            boolQueryBuilder.should(buildQuestionQuery(questionAnswerDto));
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

    public List<GeoPointDto> findClusteredGeoPoints(GeoPoint position,
                                                    double distance,
                                                    BoundingBoxDto boundingBoxDto) {
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
            .setSize(DEFAULT_MAX_RESULT_SIZE) // Hide results, only aggregation relevant
            .get();
        return extractOrganisations(searchResponse)
            .stream()
            .map(OrganisationDto::getAddresses)
            .filter(addresses -> !addresses.isEmpty())
            .map(addresses -> addresses.get(0))
            .map(AddressDto::getLocation)
            .collect(Collectors.toList());
    }

    private QueryBuilder buildQuestionQuery(QuestionAnswerDto questionAnswerDto) {
        BoolQueryBuilder questionQuery = boolQuery()
            .minimumNumberShouldMatch(1)
            .must(termQuery("questions.uid", questionAnswerDto.getId()))
            .should(termQuery("questions.answer", questionAnswerDto.getAnswer().toString()).boost(2.0f));

        for (AnswerDto neighbour : questionAnswerDto.getAnswer().getNeighbours()) {
            questionQuery.should(termQuery("questions.answer", neighbour.toString()).boost(1.0f));
        }

        return nestedQuery("questions", questionQuery);
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
        List<ContactPersonDto> contactPersonDtos = extractContactPersons(source);
        return new OrganisationDto(
            (String) source.get("id"),
            (String) source.get("name"),
            (String) source.get("description"),
            (String) source.get("website"),
            (String) source.get("mapPin"),
            addresses.stream().map(this::extractAddress).collect(Collectors.toList()),
            contactPersonDtos,
            extractLogoId(source)
        );
    }

    @SuppressWarnings("unchecked")
    private List<ContactPersonDto> extractContactPersons(Map<String, Object> source) {
        List<Map<String, Object>> contactPersons = (List<Map<String, Object>>) source.get("contactPersons");
        if (contactPersons == null) {
            return Collections.emptyList();
        }
        return contactPersons.stream().map(this::extractContactPerson).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private String extractLogoId(Map<String, Object> source) {
        Map<String, Object> logo = (Map<String, Object>) source.get("logo");
        if (logo == null) {
            return null;
        }
        return (String) logo.get("value");
    }

    private ContactPersonDto extractContactPerson(Map<String, Object> contactPerson) {
        return new ContactPersonDto.Builder()
            .setFirstname((String) contactPerson.get("firstname"))
            .setLastname((String) contactPerson.get("lastname"))
            .setRank((String) contactPerson.get("rank"))
            .setTelephone((String) contactPerson.get("telephone"))
            .build();
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
