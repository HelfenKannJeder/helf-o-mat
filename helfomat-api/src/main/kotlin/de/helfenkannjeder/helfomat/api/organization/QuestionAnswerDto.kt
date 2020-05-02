package de.helfenkannjeder.helfomat.api.organization

import de.helfenkannjeder.helfomat.core.organization.Answer

/**
 * @author Valentin Zickner
 */
data class QuestionAnswerDto (
    val id: String,
    val answer: Answer
)