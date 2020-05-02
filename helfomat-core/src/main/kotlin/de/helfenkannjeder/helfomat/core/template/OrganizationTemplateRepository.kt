package de.helfenkannjeder.helfomat.core.template

import de.helfenkannjeder.helfomat.core.organization.OrganizationType

/**
 * @author Valentin Zickner
 */
interface OrganizationTemplateRepository {
    fun findByOrganizationType(organizationType: OrganizationType): OrganizationTemplate?
}