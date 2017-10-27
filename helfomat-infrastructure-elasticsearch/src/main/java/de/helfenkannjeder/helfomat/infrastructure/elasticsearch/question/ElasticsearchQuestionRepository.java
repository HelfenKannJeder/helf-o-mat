package de.helfenkannjeder.helfomat.infrastructure.elasticsearch.question;

import de.helfenkannjeder.helfomat.core.question.Question;
import de.helfenkannjeder.helfomat.core.question.QuestionRepository;
import de.helfenkannjeder.helfomat.infrastructure.elasticsearch.ElasticsearchConfiguration;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.Nested;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Valentin Zickner
 */
@Component
public class ElasticsearchQuestionRepository implements QuestionRepository {

    private static final int DEFAULT_MAX_RESULT_SIZE = 10000;
    private Client client;
    private ElasticsearchConfiguration elasticsearchConfiguration;

    @Autowired
    public ElasticsearchQuestionRepository(Client client,
                                           ElasticsearchConfiguration elasticsearchConfiguration) {
        this.client = client;
        this.elasticsearchConfiguration = elasticsearchConfiguration;
    }

    public List<Question> findQuestions() {
        Nested nested = client.prepareSearch(this.elasticsearchConfiguration.getIndex())
            .setTypes(this.elasticsearchConfiguration.getType().getOrganisation())
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

    private Question bucketToQuestion(Terms.Bucket s) {
        long id = Long.valueOf(this.getSubbucket(s, "id", this::convertIntBucket).orElse(0));
        String question = s.getKeyAsString();
        String description = this.getSubbucket(s, "description", this::convertStringBucket).orElse(null);
        Integer position = this.getSubbucket(s, "position", this::convertIntBucket).orElse(0);
        return new Question(id, question, description, position);
    }

    private int sortQuestions(Question q1, Question q2) {
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
