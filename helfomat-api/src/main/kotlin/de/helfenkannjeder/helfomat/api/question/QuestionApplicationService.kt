package de.helfenkannjeder.helfomat.api.question

import de.helfenkannjeder.helfomat.core.question.QuestionRepository
import org.springframework.stereotype.Service

/**
 * @author Valentin Zickner
 */
@Service
class QuestionApplicationService(
    private val questionRepository: QuestionRepository
) {

    fun findQuestions(): List<QuestionDto> = questionRepository.findQuestions()
        .map { QuestionDto(it.id.value, it.question, it.description) }

}