package de.helfenkannjeder.helfomat.api.question;

import de.helfenkannjeder.helfomat.core.question.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Valentin Zickner
 */
@Service
public class QuestionApplicationService {
    private QuestionRepository questionRepository;

    public QuestionApplicationService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public List<QuestionDto> findQuestions() {
        return this.questionRepository.findQuestions()
            .stream()
            .map(question -> new QuestionDto(
                question.getId().getValue(),
                question.getQuestion(),
                question.getDescription()
            ))
            .collect(Collectors.toList());
    }
}
