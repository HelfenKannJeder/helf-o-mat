package de.helfenkannjeder.helfomat.api.approval

import de.helfenkannjeder.helfomat.api.Roles
import de.helfenkannjeder.helfomat.core.approval.ApprovalRepository
import de.helfenkannjeder.helfomat.core.organization.event.ReloadOrganizationEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.access.annotation.Secured
import org.springframework.stereotype.Service

@Service
@Secured(Roles.ADMIN)
open class AdminApplicationService(
    private val approvalRepository: ApprovalRepository,
    private val applicationEventPublisher: ApplicationEventPublisher
) {

    open fun reloadAllOrganizations() {
        approvalRepository.findAll()
            .map { it.approvedDomainEvent?.organizationId }
            .filterNotNull()
            .distinct()
            .forEach { applicationEventPublisher.publishEvent(ReloadOrganizationEvent(it)) }
    }
}