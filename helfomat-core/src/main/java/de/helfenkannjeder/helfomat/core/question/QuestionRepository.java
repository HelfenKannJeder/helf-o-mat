package de.helfenkannjeder.helfomat.core.question;

import java.util.List;

/**
 * @author Valentin Zickner
 */
public interface QuestionRepository {
    List<Question> findQuestions();
}
