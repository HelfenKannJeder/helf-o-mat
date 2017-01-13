package de.helfenkannjeder.helfomat.filter;

import de.helfenkannjeder.helfomat.domain.Organisation;
import de.helfenkannjeder.helfomat.repository.OrganisationRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DuplicateOrganisationFilterProcessor implements ItemProcessor<Organisation, Organisation> {

    private OrganisationRepository organisationRepository;

    @Autowired
    public DuplicateOrganisationFilterProcessor(OrganisationRepository organisationRepository) {
        this.organisationRepository = organisationRepository;
    }

    @Override
    public Organisation process(Organisation organisation) throws Exception {
        if(organisationRepository.existsOrganisationWithSameTypeInDistance(organisation, 500L)) {
            return null;
        } else {
            return organisation;
        }
    }
}
