package de.helfenkannjeder.helfomat.api

import de.helfenkannjeder.helfomat.core.organization.Answer
import de.helfenkannjeder.helfomat.core.organization.OrganizationType
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.util.*

/**
 * @author Valentin Zickner
 */
@ConstructorBinding
@ConfigurationProperties(prefix = "helfomat")
data class QuestionConfiguration(
    var questions: List<QuestionMapping>
) {

    data class QuestionMapping(
        var id: String,
        var question: String,
        var description: String,
        var defaultAnswer: Answer,
        var groups: List<QuestionOrganizationGroupMapping> = ArrayList()
    )

    data class QuestionOrganizationGroupMapping(
        var organizationType: OrganizationType? = null,
        var phrase: String?,
        var answer: Answer
    )

}