package de.helfenkannjeder.helfomat.api.organization

import de.helfenkannjeder.helfomat.api.QuestionConfiguration
import de.helfenkannjeder.helfomat.api.QuestionConfiguration.QuestionMapping
import de.helfenkannjeder.helfomat.core.organization.Answer
import de.helfenkannjeder.helfomat.core.organization.Organization
import de.helfenkannjeder.helfomat.core.organization.QuestionAnswer
import de.helfenkannjeder.helfomat.core.question.QuestionId
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component
import java.util.stream.Collectors

/**
 * @author Valentin Zickner
 */
@Component
@EnableConfigurationProperties(QuestionConfiguration::class)
class AnswerOrganizationQuestionService(
    private val questionConfiguration: QuestionConfiguration
) {

    fun answerQuestions(organization: Organization): Organization {
        return Organization.Builder(organization)
            .setQuestionAnswers(answerAllQuestionsForOrganization(organization))
            .build()
    }

    private fun answerAllQuestionsForOrganization(organization: Organization): List<QuestionAnswer> {
        return questionConfiguration.questions
            .stream()
            .map { questionMapping: QuestionMapping ->
                QuestionAnswer(
                    QuestionId(questionMapping.id),
                    answerQuestionForOrganization(questionMapping, organization)
                )
            }
            .collect(Collectors.toList())
    }

    private fun answerQuestionForOrganization(question: QuestionMapping, organization: Organization): Answer {
        return question.groups
            .filter { (it.organizationType == null || organization.organizationType == it.organizationType) && organization.hasGroupWithPhrase(it.phrase) }
            .map { it.answer }
            .firstOrNull()
            ?: question.defaultAnswer
    }

}

fun Organization.hasGroupWithPhrase(phrase: String?) = phrase == null || this.groups.any { group -> group.name.contains(phrase) }