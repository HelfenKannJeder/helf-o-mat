package de.helfenkannjeder.helfomat.web.controller

import de.helfenkannjeder.helfomat.api.approval.ApprovalApplicationService
import de.helfenkannjeder.helfomat.api.organization.event.OrganizationEventDto
import de.helfenkannjeder.helfomat.core.approval.ApprovalId
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

/**
 * @author Valentin Zickner
 */
@RestController
@RequestMapping(path = ["/api"], produces = [MediaType.APPLICATION_JSON_VALUE])
class ApprovalController(
    private val approvalApplicationService: ApprovalApplicationService
) {

    @GetMapping("/approval")
    fun findItemsToApprove() = approvalApplicationService.findItemsToApprove()

    @GetMapping("/approval/{approvalId}")
    fun getApprovalItem(@PathVariable approvalId: ApprovalId) = approvalApplicationService.findApprovalItem(approvalId)

    @PutMapping("/approval/{approvalId}")
    fun confirmApproval(@PathVariable approvalId: ApprovalId, @RequestBody confirmedEvents: List<OrganizationEventDto>) = approvalApplicationService.confirmOrganizationChange(approvalId, confirmedEvents)

}