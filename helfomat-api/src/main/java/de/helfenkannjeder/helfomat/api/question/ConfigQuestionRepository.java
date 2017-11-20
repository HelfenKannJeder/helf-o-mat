package de.helfenkannjeder.helfomat.api.question;

import de.helfenkannjeder.helfomat.api.HelfomatConfiguration;
import de.helfenkannjeder.helfomat.core.question.Question;
import de.helfenkannjeder.helfomat.core.question.QuestionId;
import de.helfenkannjeder.helfomat.core.question.QuestionRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ConfigQuestionRepository implements QuestionRepository {

    private final HelfomatConfiguration helfomatConfiguration;

    public ConfigQuestionRepository(HelfomatConfiguration helfomatConfiguration) {
        this.helfomatConfiguration = helfomatConfiguration;
    }

    public List<Question> findQuestions() {
        return this.helfomatConfiguration.getQuestions()
            .stream()
            .map(questionMapping -> {
                Question question = new Question();
                question.setId(new QuestionId(questionMapping.getId()));
                question.setQuestion(questionMapping.getQuestion());
                question.setDescription(questionMapping.getDescription());
                return question;
            })
            .collect(Collectors.toList());
    }

}
