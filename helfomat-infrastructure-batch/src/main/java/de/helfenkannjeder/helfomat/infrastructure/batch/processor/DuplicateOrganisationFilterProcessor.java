package de.helfenkannjeder.helfomat.infrastructure.batch.processor;

import de.helfekannjeder.helfomat.core.IndexManager;
import de.helfekannjeder.helfomat.core.organisation.Organisation;
import de.helfekannjeder.helfomat.core.organisation.OrganisationRepository;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@JobScope
public class DuplicateOrganisationFilterProcessor implements ItemProcessor<Organisation, Organisation> {

    private final OrganisationRepository organisationRepository;
    private final IndexManager indexManager;

    @Autowired
    public DuplicateOrganisationFilterProcessor(OrganisationRepository organisationRepository,
                                                IndexManager indexManager) {
        this.organisationRepository = organisationRepository;
        this.indexManager = indexManager;
    }

    @Override
    public Organisation process(Organisation organisation) throws Exception {
        if(organisationRepository.existsOrganisationWithSameTypeInDistance(this.indexManager.getCurrentIndex(), organisation, 500L)) {
            return null;
        } else {
            return organisation;
        }
    }
}
