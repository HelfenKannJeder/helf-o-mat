package de.helfenkannjeder.helfomat.api.question

import de.helfenkannjeder.helfomat.core.organization.Answer
import de.helfenkannjeder.helfomat.core.question.QuestionId

/**
 * @author Valentin Zickner
 */
data class AnsweredQuestionDto(
    var questionId: QuestionId,
    var question: String,
    var answer: Answer
)