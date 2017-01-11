package de.helfenkannjeder.helfomat.controller;

import de.helfenkannjeder.helfomat.dto.OrganisationDetailDto;
import de.helfenkannjeder.helfomat.service.OrganisationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Valentin Zickner
 */
@RestController
@RequestMapping("/organisation")
public class OrganisationController {

    private final OrganisationService organisationService;

    @Autowired
    public OrganisationController(OrganisationService organisationService) {
        this.organisationService = organisationService;
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public OrganisationDetailDto getOrganisation(@PathVariable String id) {
        return OrganisationDetailDto.fromOrganisation(this.organisationService.getOrganisation(id));
    }
}
