package de.helfenkannjeder.helfomat.api.approval

import de.helfenkannjeder.helfomat.api.organization.OrganizationDetailDto
import de.helfenkannjeder.helfomat.api.organization.event.OrganizationEventDto
import de.helfenkannjeder.helfomat.core.approval.ApprovalId
import java.time.OffsetDateTime

data class ApprovalDetailDto(
    val approvalId: ApprovalId,
    val organizationName: String?,
    val organizationUrl: String?,
    val date: OffsetDateTime?,
    val organization: OrganizationDetailDto?,
    val proposedDomainEvent: OrganizationEventDto?,
    val approvedDomainEvent: OrganizationEventDto?,
    val author: ApprovalAuthorDto?,
    val reviewed: Boolean,
    val approved: Boolean
)
