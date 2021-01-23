package de.helfenkannjeder.helfomat.api.approval

import de.helfenkannjeder.helfomat.api.organization.event.toOrganizationEventDto
import de.helfenkannjeder.helfomat.api.organization.toOrganizationDetailDto
import de.helfenkannjeder.helfomat.core.approval.Approval
import de.helfenkannjeder.helfomat.core.organization.Organization
import de.helfenkannjeder.helfomat.core.organization.OrganizationId
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationCreateEvent
import de.helfenkannjeder.helfomat.core.organization.event.ProposedChangeOrganizationEvent
import de.helfenkannjeder.helfomat.core.question.Question
import de.helfenkannjeder.helfomat.core.user.User

fun Approval.toApprovalOverviewDto(organizationId: OrganizationId, organization: Organization?) =
    ApprovalOverviewDto(
        this.approvalId,
        organizationId,
        organization?.name ?: this.requestedDomainEvent.getNameForNewOrganization(),
        organization?.urlName,
        this.requestedDomainEvent.author,
        this.approvedDomainEvent?.approvedBy,
        !this.isDeclined,
        this.createdDate,
        this.requestedDomainEvent.sources
    )

fun Approval.toApprovalDetailDto(organization: Organization?, questions: List<Question>, author: User?, approved: Boolean) = ApprovalDetailDto(
    this.approvalId,
    organization?.name ?: this.requestedDomainEvent.getNameForNewOrganization(),
    this.createdDate,
    organization?.toOrganizationDetailDto(questions),
    this.requestedDomainEvent.toOrganizationEventDto(questions, organization),
    this.approvedDomainEvent?.toOrganizationEventDto(questions, organization),
    author?.toApprovalAuthorDto(),
    approved,
    !this.isDeclined
)

fun ProposedChangeOrganizationEvent.getNameForNewOrganization(): String? {
    val organizationCreateEvent = this.changes.firstOrNull { it is OrganizationCreateEvent } ?: return null
    return (organizationCreateEvent as OrganizationCreateEvent).name
}

fun User.toApprovalAuthorDto(): ApprovalAuthorDto? = ApprovalAuthorDto(this.username, this.email, this.firstName, this.lastName)