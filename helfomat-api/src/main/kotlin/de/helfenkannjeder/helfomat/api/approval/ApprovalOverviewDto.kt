package de.helfenkannjeder.helfomat.api.approval

import de.helfenkannjeder.helfomat.core.approval.ApprovalId
import de.helfenkannjeder.helfomat.core.organization.OrganizationId
import java.time.OffsetDateTime

/**
 * @author Valentin Zickner
 */
data class ApprovalOverviewDto(
    val approvalId: ApprovalId,
    val organizationId: OrganizationId,
    val organizationName: String?,
    val organizationUrl: String?,
    val author: String,
    val approvedBy: String?,
    val date: OffsetDateTime?,
    val sources: String
)