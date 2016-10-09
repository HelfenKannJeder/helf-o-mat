package de.helfenkannjeder.helfomat.service;

import de.helfenkannjeder.helfomat.domain.Answer;
import de.helfenkannjeder.helfomat.domain.GeoPoint;
import de.helfenkannjeder.helfomat.domain.Question;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.Nested;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.geoDistanceQuery;
import static org.elasticsearch.index.query.QueryBuilders.nestedQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

/**
 * @author Valentin Zickner
 */
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

    public List<Map<String, Object>> findOrganisation(List<Answer> answers, GeoPoint position, double distance) {
        BoolQueryBuilder boolQueryBuilder = boolQuery();
        for (Answer answer : answers) {
            if (answer.getAnswer() == -1 || answer.getAnswer() == 1) {
                String answerString = "NO";
                if (answer.getAnswer() == 1) {
                    answerString = "YES";
                }
                boolQueryBuilder.should(
                        nestedQuery("questions",
                                boolQuery()
                                        .must(termQuery("questions.uid", answer.getId()))
                                        .must(termQuery("questions.answer", answerString))
                        )
                );
            }
        }

        boolQueryBuilder.filter(
                nestedQuery("addresses",
                        geoDistanceQuery("addresses.location")
                                .lat(position.getLat())
                                .lon(position.getLon())
                                .distance(distance, DistanceUnit.KILOMETERS)
                )
        );

        SortBuilder sortBuilder =
                SortBuilders
                        .geoDistanceSort("addresses.location")
                        .setNestedPath("addresses")
                        .point(position.getLat(), position.getLon())
                        .unit(DistanceUnit.KILOMETERS)
                        .order(SortOrder.DESC);
        return executeQueryAndExtractResult(boolQueryBuilder, sortBuilder);
    }

    private List<Map<String, Object>> executeQueryAndExtractResult(QueryBuilder queryBuilder, SortBuilder sortBuilder) {
        List<Map<String, Object>> organisations = new ArrayList<>();
        SearchHits hits = executeQuery(queryBuilder, sortBuilder).getHits();
        Float maxScore = null;

        for (SearchHit hit : hits.getHits()) {
            if (maxScore == null) {
                maxScore = hit.getScore();
            }

            Map<String, Object> response = new HashMap<>(hit.getSource());
            response.put("_score", hit.getScore());
            response.put("_scoreNorm", (hit.getScore() * 100) / maxScore);
            organisations.add(response);
        }
        return organisations;
    }

    private SearchResponse executeQuery(QueryBuilder queryBuilder, SortBuilder sortBuilder) {
        return client
                .prepareSearch(index)
                .setTypes(type)
                .setQuery(queryBuilder)
                .setSize(100)
                .addSort(SortBuilders.scoreSort())
                .addSort(sortBuilder)
                .execute()
                .actionGet();
    }

    public List<Question> findQuestions() {
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
                .map(s -> {
                    Question question = new Question(s.getKeyAsString());
                    question.setId(String.valueOf(getSubbucketInteger(s, "id")));
                    question.setDescription(getSubbucketString(s, "description"));
                    question.setPosition(getSubbucketInteger(s, "position"));
                    return question;
                })
                .sorted((q1, q2) -> q1.getPosition() < q2.getPosition() ? -1 : 1)
                .collect(Collectors.toList());

    }

    private String getSubbucketString(Terms.Bucket bucket, String aggregationName) {
        StringTerms descriptionBucket = bucket.getAggregations().get(aggregationName);
        Terms.Bucket resultBucket = null;
        if (descriptionBucket.getBuckets().size() == 1) {
            return descriptionBucket.getBuckets().get(0).getKeyAsString();
        }
        return null;
    }

    private int getSubbucketInteger(Terms.Bucket bucket, String aggregationName) {
        LongTerms descriptionBucket = bucket.getAggregations().get(aggregationName);
        if (descriptionBucket.getBuckets().size() == 1) {
            return descriptionBucket.getBuckets().get(0).getKeyAsNumber().intValue();
        }
        return 0;
    }

}
