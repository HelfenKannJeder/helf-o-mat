package de.helfenkannjeder.helfomat.web.controller;

import de.helfenkannjeder.helfomat.api.geopoint.TravelDistanceDto;
import de.helfenkannjeder.helfomat.api.organisation.OrganisationApplicationService;
import de.helfenkannjeder.helfomat.api.organisation.TravelDistanceApplicationService;
import de.helfenkannjeder.helfomat.api.search.OrganisationDetailDto;
import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint;
import de.helfenkannjeder.helfomat.core.organisation.Organisation;
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

    private final OrganisationApplicationService organisationApplicationService;
    private final TravelDistanceApplicationService travelDistanceApplicationService;

    @Autowired
    public OrganisationController(OrganisationApplicationService organisationApplicationService, TravelDistanceApplicationService travelDistanceApplicationService) {
        this.organisationApplicationService = organisationApplicationService;
        this.travelDistanceApplicationService = travelDistanceApplicationService;
    }

    @GetMapping("/{id}")
    public OrganisationDetailDto getOrganisation(@PathVariable String id) {
        return OrganisationDetailDto.fromOrganisation(this.organisationApplicationService.findOne(id));
    }

    @GetMapping("/{id}/travelDistances")
    public List<TravelDistanceDto> getTravelDistances(@PathVariable String id, @RequestParam("lat") Double lat, @RequestParam("lon") Double lon) {
        Organisation organisation = organisationApplicationService.findOne(id);
        GeoPoint origin = new GeoPoint(lat, lon);
        return travelDistanceApplicationService.requestTravelDistances(organisation, origin);
    }
}
