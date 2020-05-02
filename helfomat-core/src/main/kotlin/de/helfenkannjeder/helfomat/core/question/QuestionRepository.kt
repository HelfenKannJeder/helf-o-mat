package de.helfenkannjeder.helfomat.core.question

/**
 * @author Valentin Zickner
 */
interface QuestionRepository {
    fun findQuestions(): List<Question>
}