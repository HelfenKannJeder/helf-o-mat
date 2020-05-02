package de.helfenkannjeder.helfomat.api.organization

import de.helfenkannjeder.helfomat.core.organization.QuestionAnswer
import de.helfenkannjeder.helfomat.core.question.QuestionId

/**
 * @author Valentin Zickner
 */
fun List<QuestionAnswerDto>.toQuestionAnswers() = this.map { it.toQuestionAnswer() }
fun QuestionAnswerDto.toQuestionAnswer() = QuestionAnswer(QuestionId(this.id), this.answer)