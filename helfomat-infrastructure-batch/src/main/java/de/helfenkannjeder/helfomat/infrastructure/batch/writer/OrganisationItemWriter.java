package de.helfenkannjeder.helfomat.infrastructure.batch.writer;

import de.helfenkannjeder.helfomat.core.IndexManager;
import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationRepository;
import org.apache.log4j.Logger;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Valentin Zickner
 */
@Component
@JobScope
public class OrganisationItemWriter implements ItemWriter<Organisation> {

    private static final Logger LOGGER = Logger.getLogger(OrganisationItemWriter.class);

    private final IndexManager indexManager;
    private final OrganisationRepository organisationRepository;

    public OrganisationItemWriter(IndexManager indexManager,
                                  OrganisationRepository organisationRepository) {
        this.indexManager = indexManager;
        this.organisationRepository = organisationRepository;
    }

    @Override
    public void write(List<? extends Organisation> items) throws Exception {
        items.forEach(organisation -> LOGGER.debug("Write organisation '" + organisation.getName() + "'"));

        this.organisationRepository.save(this.indexManager.getCurrentIndex(), items);
    }
}
