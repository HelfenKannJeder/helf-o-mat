package de.helfenkannjeder.helfomat.api.approval

import de.helfenkannjeder.helfomat.api.organization.event.toOrganizationEventDto
import de.helfenkannjeder.helfomat.api.organization.toOrganizationDetailDto
import de.helfenkannjeder.helfomat.core.approval.Approval
import de.helfenkannjeder.helfomat.core.organization.Organization
import de.helfenkannjeder.helfomat.core.organization.OrganizationId
import de.helfenkannjeder.helfomat.core.question.Question

fun Approval.toApprovalOverviewDto(organizationId: OrganizationId, organization: Organization?) =
    ApprovalOverviewDto(
        this.approvalId,
        organizationId,
        organization?.name,
        this.requestedDomainEvent.author,
        this.createdDate,
        this.requestedDomainEvent.sources
    )

fun Approval.toApprovalDetailDto(organization: Organization?, questions: List<Question>) = ApprovalDetailDto(
    this.approvalId,
    this.createdDate,
    organization?.toOrganizationDetailDto(questions),
    this.requestedDomainEvent.toOrganizationEventDto(questions)
)