package de.helfenkannjeder.helfomat.infrastructure.config;

import de.helfenkannjeder.helfomat.api.QuestionConfiguration;
import de.helfenkannjeder.helfomat.core.question.Question;
import de.helfenkannjeder.helfomat.core.question.QuestionId;
import de.helfenkannjeder.helfomat.core.question.QuestionRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ConfigQuestionRepository implements QuestionRepository {

    private final QuestionConfiguration questionConfiguration;

    public ConfigQuestionRepository(QuestionConfiguration questionConfiguration) {
        this.questionConfiguration = questionConfiguration;
    }

    public List<Question> findQuestions() {
        return this.questionConfiguration.getQuestions()
            .stream()
            .map(questionMapping -> new Question(
                new QuestionId(questionMapping.getId()),
                questionMapping.getQuestion(),
                questionMapping.getDescription()
            ))
            .collect(Collectors.toList());
    }

}
