package de.helfenkannjeder.helfomat.controller;

import de.helfenkannjeder.helfomat.dto.OrganisationDetailDto;
import de.helfenkannjeder.helfomat.service.OrganisationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Valentin Zickner
 */
@RestController
@RequestMapping(path = "/organisation", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrganisationController {

    private final OrganisationService organisationService;

    @Autowired
    public OrganisationController(OrganisationService organisationService) {
        this.organisationService = organisationService;
    }

    @GetMapping("/{id}")
    public OrganisationDetailDto getOrganisation(@PathVariable String id) {
        return OrganisationDetailDto.fromOrganisation(this.organisationService.getOrganisation(id));
    }
}
