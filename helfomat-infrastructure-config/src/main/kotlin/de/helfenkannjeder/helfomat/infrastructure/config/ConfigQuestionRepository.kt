package de.helfenkannjeder.helfomat.infrastructure.config

import de.helfenkannjeder.helfomat.api.QuestionConfiguration
import de.helfenkannjeder.helfomat.core.question.Question
import de.helfenkannjeder.helfomat.core.question.QuestionId
import de.helfenkannjeder.helfomat.core.question.QuestionRepository
import org.springframework.stereotype.Component

@Component
class ConfigQuestionRepository(private val questionConfiguration: QuestionConfiguration) : QuestionRepository {

    override fun findQuestions(): List<Question> {
        return questionConfiguration.questions
            .map {
                Question(
                    QuestionId(it.id),
                    it.question,
                    it.description
                )
            }
    }

}