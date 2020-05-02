package de.helfenkannjeder.helfomat.infrastructure.batch.listener

import de.helfenkannjeder.helfomat.core.organization.Organization
import org.slf4j.LoggerFactory
import org.springframework.batch.core.ExitStatus
import org.springframework.batch.core.StepExecution
import org.springframework.batch.core.StepExecutionListener
import java.util.*
import java.util.function.Function

/**
 * @author Valentin Zickner
 */
class UniqueOrganizationUrlNameOrganizationProcessor : Function<Organization, Organization>, StepExecutionListener {
    private val LOG = LoggerFactory.getLogger(UniqueOrganizationUrlNameOrganizationProcessor::class.java)
    private val organizationUrlNames: MutableSet<String> = HashSet()

    override fun apply(organization: Organization): Organization {
        val organizationUrlName = determineOrganizationUrlName(organization)
        return Organization.Builder(organization)
            .setUrlName(organizationUrlName)
            .build()
    }

    @Synchronized
    private fun determineOrganizationUrlName(organization: Organization): String {
        var organizationUrlName = toUrlName(organization.name)
        var i = 0
        val originalOrganizationUrlName = organizationUrlName
        while (organizationUrlNames.contains(organizationUrlName)) {
            organizationUrlName = originalOrganizationUrlName + "-" + ++i
            LOG.info("Organization name for '$originalOrganizationUrlName' does already exists, choose new name '$organizationUrlName'")
        }
        organizationUrlNames.add(organizationUrlName)
        return organizationUrlName
    }

    override fun beforeStep(stepExecution: StepExecution) {
        organizationUrlNames.clear()
    }

    override fun afterStep(stepExecution: StepExecution): ExitStatus {
        return stepExecution.exitStatus
    }

    companion object {
        private fun toUrlName(organizationName: String): String {
            return organizationName
                .toLowerCase()
                .replace("ä".toRegex(), "ae")
                .replace("ö".toRegex(), "oe")
                .replace("ü".toRegex(), "ue")
                .replace("ß".toRegex(), "ss")
                .replace(" ".toRegex(), "-")
                .replace("[^a-z0-9\\-]".toRegex(), "")
        }
    }
}