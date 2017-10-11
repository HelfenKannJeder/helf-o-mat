package de.helfenkannjeder.helfomat.controller;

import de.helfenkannjeder.helfomat.domain.GeoPoint;
import de.helfenkannjeder.helfomat.domain.Organisation;
import de.helfenkannjeder.helfomat.dto.OrganisationDetailDto;
import de.helfenkannjeder.helfomat.dto.TravelDistanceDto;
import de.helfenkannjeder.helfomat.service.OrganisationService;
import de.helfenkannjeder.helfomat.service.TravelDistanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Valentin Zickner
 */
@RestController
@RequestMapping(path = "/organisation", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrganisationController {

    private final OrganisationService organisationService;
    private final TravelDistanceService travelDistanceService;

    @Autowired
    public OrganisationController(OrganisationService organisationService, TravelDistanceService travelDistanceService) {
        this.organisationService = organisationService;
        this.travelDistanceService = travelDistanceService;
    }

    @GetMapping("/{id}")
    public OrganisationDetailDto getOrganisation(@PathVariable String id) {
        return OrganisationDetailDto.fromOrganisation(this.organisationService.getOrganisation(id));
    }

    @GetMapping("/{id}/travelDistances")
    public List<TravelDistanceDto> getTravelDistances(@PathVariable String id, @RequestParam("lat") Double lat, @RequestParam("lng") Double lng) {
        Organisation organisation = organisationService.getOrganisation(id);
        GeoPoint origin = new GeoPoint(lat, lng);
        return travelDistanceService.requestTravelDistances(organisation, origin);
    }
}
