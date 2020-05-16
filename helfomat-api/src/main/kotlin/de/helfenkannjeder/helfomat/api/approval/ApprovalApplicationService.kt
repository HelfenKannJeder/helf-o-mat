package de.helfenkannjeder.helfomat.api.approval

import de.helfenkannjeder.helfomat.api.Roles
import de.helfenkannjeder.helfomat.api.currentUsername
import de.helfenkannjeder.helfomat.core.approval.Approval
import de.helfenkannjeder.helfomat.core.approval.ApprovalId
import de.helfenkannjeder.helfomat.core.approval.ApprovalRepository
import de.helfenkannjeder.helfomat.core.organization.NullableOrganizationEventVisitor
import de.helfenkannjeder.helfomat.core.organization.Organization
import de.helfenkannjeder.helfomat.core.organization.OrganizationRepository
import de.helfenkannjeder.helfomat.core.organization.event.*
import de.helfenkannjeder.helfomat.core.picture.PictureId
import de.helfenkannjeder.helfomat.core.picture.PictureRepository
import de.helfenkannjeder.helfomat.core.question.QuestionRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.event.EventListener
import org.springframework.security.access.annotation.Secured
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
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val pictureRepository: PictureRepository
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
            currentUsername(),
            proposedChangeOrganizationEvent.author,
            proposedChangeOrganizationEvent.sources,
            proposedChangeOrganizationEvent.changes
        )
        publishNewPictures(proposedChangeOrganizationEvent)
        approval.approvedDomainEvent = confirmedChangeOrganizationEvent
        approvalRepository.save(approval)
        applicationEventPublisher.publishEvent(confirmedChangeOrganizationEvent)
    }

    private fun publishNewPictures(proposedChangeOrganizationEvent: ProposedChangeOrganizationEvent) {
        val visitor = object : NullableOrganizationEventVisitor<List<PictureId>> {
            override fun visit(organizationEditAddPictureEvent: OrganizationEditAddPictureEvent): List<PictureId>? =
                listOf(organizationEditAddPictureEvent.pictureId)

            override fun visit(organizationEditTeaserImageEvent: OrganizationEditTeaserImageEvent): List<PictureId>? {
                val pictureId = organizationEditTeaserImageEvent.teaserImage ?: return null
                return listOf(pictureId)
            }

            override fun visit(organizationEditAddContactPersonEvent: OrganizationEditAddContactPersonEvent): List<PictureId>? {
                val pictureId = organizationEditAddContactPersonEvent.contactPerson.picture ?: return null
                return listOf(pictureId)
            }

            override fun visit(organizationEditAddVolunteerEvent: OrganizationEditAddVolunteerEvent): List<PictureId>? {
                val pictureId = organizationEditAddVolunteerEvent.volunteer.picture ?: return null
                return listOf(pictureId)
            }

            override fun visit(organizationEditLogoEvent: OrganizationEditLogoEvent): List<PictureId>? {
                val pictureId = organizationEditLogoEvent.logo ?: return null
                return listOf(pictureId)
            }
        }
        val picturesToMakePublic = proposedChangeOrganizationEvent.changes.flatMap { it.visit(visitor) ?: emptyList() }
        this.pictureRepository.saveAll(
            pictureRepository.findAllById(picturesToMakePublic)
                .map { it.apply { public = true } }
        )
    }

}