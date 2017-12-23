package de.helfenkannjeder.helfomat.infrastructure.batch.processor;

import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationRepository;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@JobScope
public class DuplicateOrganisationFilterProcessor implements ItemProcessor<Organisation, Organisation> {

    private final OrganisationRepository organisationRepository;

    @Autowired
    public DuplicateOrganisationFilterProcessor(@Qualifier("importOrganisationRepository") OrganisationRepository importOrganisationRepository) {
        this.organisationRepository = importOrganisationRepository;
    }

    @Override
    public Organisation process(Organisation organisation) throws Exception {
        if (organisationRepository.existsOrganisationWithSameTypeInDistance(organisation, 500L)) {
            return null;
        } else {
            return organisation;
        }
    }
}
