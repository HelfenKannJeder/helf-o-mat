package de.helfenkannjeder.helfomat.service;

import de.helfenkannjeder.helfomat.domain.Organisation;
import de.helfenkannjeder.helfomat.repository.OrganisationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Valentin Zickner
 */
@Service
public class OrganisationService {

    private final OrganisationRepository organisationRepository;

    @Autowired
    public OrganisationService(OrganisationRepository organisationRepository) {
        this.organisationRepository = organisationRepository;
    }

    public Organisation getOrganisation(String id) {
        return this.organisationRepository.findOne(id);
    }

}
