package de.helfenkannjeder.helfomat.api.approval

import de.helfenkannjeder.helfomat.api.Roles
import de.helfenkannjeder.helfomat.core.approval.Approval
import de.helfenkannjeder.helfomat.core.approval.ApprovalId
import de.helfenkannjeder.helfomat.core.approval.ApprovalRepository
import de.helfenkannjeder.helfomat.core.organization.Organization
import de.helfenkannjeder.helfomat.core.organization.OrganizationRepository
import de.helfenkannjeder.helfomat.core.organization.event.ConfirmedChangeOrganizationEvent
import de.helfenkannjeder.helfomat.core.organization.event.ProposedChangeOrganizationEvent
import de.helfenkannjeder.helfomat.core.question.QuestionRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.event.EventListener
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service


/**
 * @author Valentin Zickner
 */
@Service
@Secured(Roles.ADMIN, Roles.REVIEWER)
open class ApprovalApplicationService(
    private val approvalRepository: ApprovalRepository,
    private val organizationRepository: OrganizationRepository,
    private val questionRepository: QuestionRepository,
    private val applicationEventPublisher: ApplicationEventPublisher
) {

    @EventListener
    open fun listenForProposals(proposedChangeOrganizationEvent: ProposedChangeOrganizationEvent) {
        approvalRepository.save(Approval(ApprovalId(), proposedChangeOrganizationEvent))
    }

    open fun findItemsToApprove(): List<ApprovalOverviewDto> {
        return approvalRepository.findToApprove()
            .map {
                val organizationId = it.requestedDomainEvent.organizationId
                val organization: Organization? = organizationRepository.findOne(organizationId.value)
                it.toApprovalOverviewDto(organizationId, organization)
            }
    }

    open fun findApprovalItem(approvalId: ApprovalId): ApprovalDetailDto {
        val questions = this.questionRepository.findQuestions()
        val approval = approvalRepository.getOne(approvalId)
        val organizationId = approval.requestedDomainEvent.organizationId
        val organization: Organization? = organizationRepository.findOne(organizationId.value)
        return approval.toApprovalDetailDto(organization, questions)
    }

    open fun confirmOrganizationChange(approvalId: ApprovalId) {
        val approval = this.approvalRepository.getOne(approvalId)
        val proposedChangeOrganizationEvent = approval.requestedDomainEvent
        val confirmedChangeOrganizationEvent = ConfirmedChangeOrganizationEvent(
            proposedChangeOrganizationEvent.organizationId,
            currentUser,
            proposedChangeOrganizationEvent.author,
            proposedChangeOrganizationEvent.sources,
            proposedChangeOrganizationEvent.changes
        )
        approval.approvedDomainEvent = confirmedChangeOrganizationEvent
        approvalRepository.save(approval)
        applicationEventPublisher.publishEvent(confirmedChangeOrganizationEvent)
    }

    private val currentUser get() = SecurityContextHolder.getContext().authentication.name

}