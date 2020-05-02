package de.helfenkannjeder.helfomat.api.template

import de.helfenkannjeder.helfomat.core.organization.OrganizationType
import de.helfenkannjeder.helfomat.core.template.OrganizationTemplateRepository
import org.springframework.stereotype.Service

@Service
class OrganizationTemplateApplicationService(
    private val organizationTemplateRepository: OrganizationTemplateRepository
) {

    fun findOrganizationTemplateByType(organizationType: OrganizationType): OrganizationTemplateDto {
        return organizationTemplateRepository.findByOrganizationType(organizationType)
            .toOrganizationTemplateDto()
    }

}