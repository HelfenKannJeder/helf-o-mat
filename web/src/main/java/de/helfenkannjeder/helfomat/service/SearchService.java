package de.helfenkannjeder.helfomat.service;

import de.helfenkannjeder.helfomat.domain.Question;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.Nested;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

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

    public List<Map<String, Object>> findOrganisation() {
        return executeQueryAndExtractResult(matchAllQuery());
    }

    private List<Map<String, Object>> executeQueryAndExtractResult(MatchAllQueryBuilder queryBuilder) {
        List<Map<String, Object>> organisations = new ArrayList<>();
        SearchHits hits = executeQuery(queryBuilder).getHits();
        for (SearchHit hit : hits.getHits()) {
            organisations.add(hit.getSource());
        }
        return organisations;
    }

    private SearchResponse executeQuery(MatchAllQueryBuilder queryBuilder) {
        return client
                .prepareSearch(index)
                .setTypes(type)
                .setQuery(queryBuilder)
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
