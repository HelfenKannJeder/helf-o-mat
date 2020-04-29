package de.helfenkannjeder.helfomat.core.question

/**
 * @author Valentin Zickner
 */
data class Question(
    var id: QuestionId,
    var question: String,
    var description: String
)