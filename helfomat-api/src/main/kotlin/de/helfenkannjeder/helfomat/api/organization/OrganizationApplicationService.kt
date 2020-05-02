package de.helfenkannjeder.helfomat.api.organization

import de.helfenkannjeder.helfomat.api.Roles
import de.helfenkannjeder.helfomat.api.organization.event.OrganizationCreateEventDto
import de.helfenkannjeder.helfomat.api.organization.event.OrganizationEventAssembler
import de.helfenkannjeder.helfomat.api.organization.event.OrganizationEventDto
import de.helfenkannjeder.helfomat.api.organization.event.OrganizationEventDtoAssembler
import de.helfenkannjeder.helfomat.core.geopoint.BoundingBox
import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint
import de.helfenkannjeder.helfomat.core.organization.OrganizationId
import de.helfenkannjeder.helfomat.core.organization.OrganizationRepository
import de.helfenkannjeder.helfomat.core.organization.event.ConfirmedChangeOrganizationEvent
import de.helfenkannjeder.helfomat.core.organization.event.ProposedChangeOrganizationEvent
import de.helfenkannjeder.helfomat.core.question.QuestionRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.event.EventListener
import org.springframework.security.access.annotation.Secured
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

/**
 * @author Valentin Zickner
 */
@Service
open class OrganizationApplicationService(
    private var organizationRepository: OrganizationRepository,
    private var questionRepository: QuestionRepository,
    private var applicationEventPublisher: ApplicationEventPublisher) {

    open fun findOrganizationDetails(urlName: String): OrganizationDetailDto? {
        val organization = organizationRepository.findByUrlName(urlName)
        val questions = questionRepository.findQuestions()
        return organization?.toOrganizationDetailDto(questions)
    }

    open fun findGlobalOrganizations(): List<OrganizationDto> {
        return organizationRepository.findGlobalOrganizations().toOrganizationDtos()
    }

    open fun findGlobalOrganizationsWith(questionAnswerDtos: List<QuestionAnswerDto>): List<OrganizationDto> {
        return organizationRepository.findGlobalOrganizationsByQuestionAnswersSortByAnswerMatch(questionAnswerDtos.toQuestionAnswers())
            .toScoredOrganizationDtos()
    }

    open fun findOrganizationsWith(position: GeoPoint, distance: Double): List<OrganizationDto> {
        return organizationRepository.findOrganizationsByDistanceSortByDistance(position, distance).toOrganizationDtos()

    }

    open fun findOrganizationsWith(questionAnswerDtos: List<QuestionAnswerDto>, position: GeoPoint, distance: Double): List<OrganizationDto> {
        return organizationRepository.findOrganizationsByQuestionAnswersAndDistanceSortByAnswerMatchAndDistance(
            questionAnswerDtos.toQuestionAnswers(),
            position,
            distance
        ).toScoredOrganizationDtos()
    }

    open fun findClusteredGeoPoints(position: GeoPoint?, distance: Double, boundingBox: BoundingBox): List<GeoPoint> {
        return organizationRepository.findGeoPointsOfOrganizationsInsideBoundingBox(position, distance, boundingBox)
    }

    open fun compareOrganizations(compareOrganizationDto: CompareOrganizationDto): List<OrganizationEventDto> {
        val original = compareOrganizationDto.original.toOrganization()
        val updated = compareOrganizationDto.updated.toOrganization()
        val questions = questionRepository.findQuestions()
        return OrganizationEventAssembler.toOrganizationEventDto(updated.compareTo(original), questions)
    }

    @PreAuthorize("isAuthenticated()")
    open fun submitOrganization(organizationSubmitEventDto: OrganizationSubmitEventDto) {
        val organizationEvents = OrganizationEventDtoAssembler.toOrganizationEvent(organizationSubmitEventDto.events)
        val organizationId = organizationSubmitEventDto.organizationId
        if (!isOrganizationSubmitValid(organizationId, organizationSubmitEventDto.events)) {
            throw OrganizationNotFoundException(organizationId)
        }
        val proposedChangeOrganizationEvent = ProposedChangeOrganizationEvent(
            organizationId,
            currentUser,
            organizationSubmitEventDto.sources,
            organizationEvents
        )
        applicationEventPublisher.publishEvent(proposedChangeOrganizationEvent)
    }

    @EventListener // TODO: should be based on an actual approval
    open fun confirmOrganizationEvent(proposedChangeOrganizationEvent: ProposedChangeOrganizationEvent) {
        val confirmedChangeOrganizationEvent = ConfirmedChangeOrganizationEvent(
            proposedChangeOrganizationEvent.organizationId,
            currentUser,
            proposedChangeOrganizationEvent.author,
            proposedChangeOrganizationEvent.sources,
            proposedChangeOrganizationEvent.changes
        )
        applicationEventPublisher.publishEvent(confirmedChangeOrganizationEvent)
    }

    @Secured(Roles.ADMIN)
    open fun findSimilarOrganizations(searchSimilarOrganizationDto: SearchSimilarOrganizationDto): List<OrganizationDetailDto> {
        val address = searchSimilarOrganizationDto.address?.toAddress()
        val organizationType = searchSimilarOrganizationDto.organizationType
        val distance = searchSimilarOrganizationDto.distanceInMeters
        val organizations = organizationRepository.findOrganizationWithSameTypeInDistance(address, organizationType, distance)
        val questions = questionRepository.findQuestions()
        return organizations.toOrganizationDetailsDto(questions)
    }

    private fun isOrganizationSubmitValid(organizationId: OrganizationId, events: List<OrganizationEventDto>): Boolean {
        val isNewOrganization = organizationRepository.findOne(organizationId.value) == null
        val isCreate = events
            .map { it.javaClass }
            .any { OrganizationCreateEventDto::class.java == it }
        return isNewOrganization && isCreate || !isNewOrganization && !isCreate
    }

    private val currentUser get() = SecurityContextHolder.getContext().authentication.name

}