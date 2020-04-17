package de.helfenkannjeder.helfomat.infrastructure.batch.writer;

import de.helfenkannjeder.helfomat.core.organization.Organization;
import de.helfenkannjeder.helfomat.core.organization.OrganizationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

/**
 * @author Valentin Zickner
 */
public class OrganizationItemWriter implements ItemWriter<Organization> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationItemWriter.class);

    private final OrganizationRepository organizationRepository;

    public OrganizationItemWriter(OrganizationRepository importOrganizationRepository) {
        this.organizationRepository = importOrganizationRepository;
    }

    @Override
    public void write(List<? extends Organization> items) {
        items.forEach(organization -> LOGGER.debug("Write organization '" + organization.getName() + "'"));

        this.organizationRepository.save(items);
    }
}
