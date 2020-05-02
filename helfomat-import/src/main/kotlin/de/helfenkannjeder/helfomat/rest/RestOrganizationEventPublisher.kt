package de.helfenkannjeder.helfomat.rest

import de.helfenkannjeder.helfomat.api.organization.OrganizationSubmitEventDto
import de.helfenkannjeder.helfomat.api.organization.event.toOrganizationEventDtos
import de.helfenkannjeder.helfomat.config.ImporterConfiguration
import de.helfenkannjeder.helfomat.core.organization.Organization
import de.helfenkannjeder.helfomat.core.organization.OrganizationId
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEvent
import de.helfenkannjeder.helfomat.core.question.Question
import de.helfenkannjeder.helfomat.core.question.QuestionRepository
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate

/**
 * @author Valentin Zickner
 */
@Component
@EnableConfigurationProperties(ImporterConfiguration::class)
class RestOrganizationEventPublisher(
    private val restTemplate: RestTemplate,
    private val questionRepository: QuestionRepository,
    private val importerConfiguration: ImporterConfiguration
) {

    fun publishEvents(organization: Organization, organizationEvents: List<OrganizationEvent>) {
        val questions = questionRepository.findQuestions()
        if (organizationEvents.isNotEmpty()) {
            val organizationSubmitEventDto = organizationEvents.toOrganizationSubmitEventDto(organization.id, questions)
            try {
                submitOrganization(organizationSubmitEventDto)
            } catch (exception: HttpClientErrorException) {
                if (HttpStatus.NOT_FOUND == exception.statusCode) {
                    // try to create complete organization
                    submitOrganization(organization.compareTo(null).toOrganizationSubmitEventDto(organization.id, questions))
                } else {
                    throw exception
                }
            }
        }
    }

    private fun submitOrganization(organizationSubmitEventDto: OrganizationSubmitEventDto): ResponseEntity<Void> {
        return restTemplate.postForEntity(importerConfiguration.webApiUrl + "/api/organization/submit", organizationSubmitEventDto, Void::class.java)
    }

}

private const val IMPORT_SOURCE = "Helf-O-Mat Import Application"

fun List<OrganizationEvent>.toOrganizationSubmitEventDto(organizationId: OrganizationId, questions: List<Question>): OrganizationSubmitEventDto {
    val organizationEventDtos = this.toOrganizationEventDtos(questions)
    return OrganizationSubmitEventDto(organizationId, IMPORT_SOURCE, organizationEventDtos)
}