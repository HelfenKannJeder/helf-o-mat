package de.helfenkannjeder.helfomat.infrastructure.batch.processor

import de.helfenkannjeder.helfomat.api.QuestionConfiguration
import de.helfenkannjeder.helfomat.api.QuestionConfiguration.QuestionMapping
import de.helfenkannjeder.helfomat.core.organization.Answer
import de.helfenkannjeder.helfomat.core.organization.Organization
import de.helfenkannjeder.helfomat.core.organization.QuestionAnswer
import de.helfenkannjeder.helfomat.core.question.QuestionId
import org.springframework.batch.item.ItemProcessor
import org.springframework.stereotype.Component
import java.util.stream.Collectors

/**
 * @author Valentin Zickner
 */
@Component
class AnswerQuestionsProcessor(
    private val questionConfiguration: QuestionConfiguration
) : ItemProcessor<Organization, Organization> {

    override fun process(organization: Organization): Organization {
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
            .filter { organization.organizationType == it.organizationType && organization.groups.any { group -> it.phrase != null && group.name.contains(it.phrase) } }
            .map { it.answer }
            .firstOrNull()
            ?: question.defaultAnswer
    }

}