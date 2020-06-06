package de.helfenkannjeder.helfomat.infrastructure.batch.processor

import de.helfenkannjeder.helfomat.core.organization.Organization
import de.helfenkannjeder.helfomat.core.organization.OrganizationId
import de.helfenkannjeder.helfomat.core.organization.OrganizationRepository
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEvent
import org.springframework.batch.item.ItemProcessor

/**
 * @author Valentin Zickner
 */
class OrganizationDifferenceProcessor(
    private val specificOrganizationRepository: OrganizationRepository,
    private val generalOrganizationRepository: OrganizationRepository
) : ItemProcessor<Organization, Pair<Organization, List<OrganizationEvent>>> {

    override fun process(updatedOrganization: Organization): Pair<Organization, List<OrganizationEvent>> {
        val originalOrganization = findClosestMatch(
            specificOrganizationRepository.findOrganizationWithSameTypeInDistance(updatedOrganization.defaultAddress, updatedOrganization.organizationType, 5L),
            updatedOrganization.urlName
        )
            ?: return generateExistingOrganizationFromOtherDatasource(updatedOrganization)
        return updatedOrganization.withOrganizationId(originalOrganization.id).updateRecordInComparisonWith(originalOrganization)
    }

    private fun generateExistingOrganizationFromOtherDatasource(updatedOrganization: Organization): Pair<Organization, List<OrganizationEvent>> {
        val alreadyAvailableOrganization = findClosestMatch(
            generalOrganizationRepository.findOrganizationWithSameTypeInDistance(updatedOrganization.defaultAddress, updatedOrganization.organizationType, 5L),
            updatedOrganization.urlName
        )
            ?: return updatedOrganization.updateRecordInComparisonWith(null)
        return Pair(
            updatedOrganization.withOrganizationId(alreadyAvailableOrganization.id),
            emptyList()
        )
    }

    private fun findClosestMatch(organizations: List<Organization>, urlName: String?): Organization? {
        return when (organizations.size) {
            1 -> organizations[0]
            else -> organizations.firstOrNull { it.urlName == urlName }
        }
    }

}

fun Organization.updateRecordInComparisonWith(originalOrganization: Organization?) = Pair(
    this,
    this.compareTo(originalOrganization)
)

fun Organization.withOrganizationId(organizationId: OrganizationId): Organization {
    return Organization.Builder(this)
        .setId(organizationId)
        .build()
}
