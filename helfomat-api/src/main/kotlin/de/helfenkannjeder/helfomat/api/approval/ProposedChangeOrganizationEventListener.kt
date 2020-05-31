package de.helfenkannjeder.helfomat.api.approval

import de.helfenkannjeder.helfomat.core.approval.Approval
import de.helfenkannjeder.helfomat.core.approval.ApprovalId
import de.helfenkannjeder.helfomat.core.approval.ApprovalRepository
import de.helfenkannjeder.helfomat.core.organization.event.ProposedChangeOrganizationEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service

@Service
open class ProposedChangeOrganizationEventListener(
    private val approvalRepository: ApprovalRepository
) {

    @EventListener
    open fun listenForProposals(proposedChangeOrganizationEvent: ProposedChangeOrganizationEvent) {
        approvalRepository.save(Approval(ApprovalId(), proposedChangeOrganizationEvent))
    }

}