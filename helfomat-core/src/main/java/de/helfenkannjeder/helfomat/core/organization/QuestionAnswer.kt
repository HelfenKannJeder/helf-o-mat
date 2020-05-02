package de.helfenkannjeder.helfomat.core.organization

import de.helfenkannjeder.helfomat.core.question.QuestionId

/**
 * @author Valentin Zickner
 */
data class QuestionAnswer(
    val questionId: QuestionId,
    val answer: Answer
)