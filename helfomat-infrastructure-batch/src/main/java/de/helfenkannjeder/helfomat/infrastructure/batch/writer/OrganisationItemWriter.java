package de.helfenkannjeder.helfomat.infrastructure.batch.writer;

import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationRepository;
import org.apache.log4j.Logger;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Valentin Zickner
 */
@Component
@JobScope
public class OrganisationItemWriter implements ItemWriter<Organisation> {

    private static final Logger LOGGER = Logger.getLogger(OrganisationItemWriter.class);

    private final OrganisationRepository organisationRepository;

    public OrganisationItemWriter(@Qualifier("importOrganisationRepository") OrganisationRepository importOrganisationRepository) {
        this.organisationRepository = importOrganisationRepository;
    }

    @Override
    public void write(List<? extends Organisation> items) throws Exception {
        items.forEach(organisation -> LOGGER.debug("Write organisation '" + organisation.getName() + "'"));

        this.organisationRepository.save(items);
    }
}
