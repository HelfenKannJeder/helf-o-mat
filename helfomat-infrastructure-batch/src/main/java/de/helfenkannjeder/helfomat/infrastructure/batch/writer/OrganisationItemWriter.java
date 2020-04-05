package de.helfenkannjeder.helfomat.infrastructure.batch.writer;

import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

/**
 * @author Valentin Zickner
 */
public class OrganisationItemWriter implements ItemWriter<Organisation> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrganisationItemWriter.class);

    private final OrganisationRepository organisationRepository;

    public OrganisationItemWriter(OrganisationRepository importOrganisationRepository) {
        this.organisationRepository = importOrganisationRepository;
    }

    @Override
    public void write(List<? extends Organisation> items) {
        items.forEach(organisation -> LOGGER.debug("Write organisation '" + organisation.getName() + "'"));

        this.organisationRepository.save(items);
    }
}
