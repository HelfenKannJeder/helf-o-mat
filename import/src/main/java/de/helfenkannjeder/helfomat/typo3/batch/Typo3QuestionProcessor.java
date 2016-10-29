package de.helfenkannjeder.helfomat.typo3.batch;

import de.helfenkannjeder.helfomat.domain.Question;
import de.helfenkannjeder.helfomat.typo3.domain.TQuestion;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * @author Valentin Zickner
 */
@Component
@JobScope
public class Typo3QuestionProcessor implements ItemProcessor<TQuestion, Question> {
    @Override
    public Question process(TQuestion item) throws Exception {
        if (item.getHelfomat() != 3) {
            return null;
        }

        Question question = new Question();
        question.setUid(item.getUid());
        question.setPosition(item.getSort());
        question.setQuestion(item.getQuestion());
        question.setDescription(item.getDescription());
        return question;
    }
}
