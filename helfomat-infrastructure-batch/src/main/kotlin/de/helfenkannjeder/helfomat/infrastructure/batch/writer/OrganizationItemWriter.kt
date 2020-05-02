package de.helfenkannjeder.helfomat.infrastructure.batch.writer

import de.helfenkannjeder.helfomat.core.organization.Organization
import de.helfenkannjeder.helfomat.core.organization.OrganizationRepository
import org.slf4j.LoggerFactory
import org.springframework.batch.item.ItemWriter

/**
 * @author Valentin Zickner
 */
class OrganizationItemWriter(
    private val organizationRepository: OrganizationRepository
) : ItemWriter<Organization> {

    override fun write(items: List<Organization>) {
        items.forEach { LOGGER.debug("Write organization '${it.name}'") }
        organizationRepository.save(items)
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(OrganizationItemWriter::class.java)
    }

}