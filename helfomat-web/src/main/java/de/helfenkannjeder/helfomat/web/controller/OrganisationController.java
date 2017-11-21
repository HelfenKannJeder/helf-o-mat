package de.helfenkannjeder.helfomat.web.controller;

import de.helfenkannjeder.helfomat.api.geopoint.TravelDistanceDto;
import de.helfenkannjeder.helfomat.api.organisation.OrganisationApplicationService;
import de.helfenkannjeder.helfomat.api.organisation.OrganisationDetailDto;
import de.helfenkannjeder.helfomat.api.organisation.OrganisationDto;
import de.helfenkannjeder.helfomat.api.organisation.QuestionAnswerDto;
import de.helfenkannjeder.helfomat.api.organisation.TravelDistanceApplicationService;
import de.helfenkannjeder.helfomat.core.geopoint.BoundingBox;
import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Valentin Zickner
 */
@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrganisationController {

    private final OrganisationApplicationService organisationApplicationService;
    private final TravelDistanceApplicationService travelDistanceApplicationService;

    @Autowired
    public OrganisationController(OrganisationApplicationService organisationApplicationService, TravelDistanceApplicationService
        travelDistanceApplicationService) {
        this.organisationApplicationService = organisationApplicationService;
        this.travelDistanceApplicationService = travelDistanceApplicationService;
    }

    @PostMapping("/organisation/search")
    public List<OrganisationDto> search(@RequestBody SearchRequestDto searchRequestDto) {
        return organisationApplicationService.findOrganisation(
            searchRequestDto.getAnswers(),
            searchRequestDto.getPosition(),
            searchRequestDto.getDistance()
        );
    }

    @PostMapping("/organisation/boundingBox")
    public List<GeoPoint> boundingBox(@RequestBody BoundingBoxRequestDto searchRequestDto) {
        return organisationApplicationService.findClusteredGeoPoints(
            searchRequestDto.getPosition(),
            searchRequestDto.getDistance(),
            searchRequestDto.getBoundingBox()
        );
    }

    @GetMapping("/organisation/{id}")
    public OrganisationDetailDto getOrganisation(@PathVariable OrganisationId id) {
        return this.organisationApplicationService.findOrganisationDetails(id);
    }

    @GetMapping("/organisation/{id}/travelDistances")
    public List<TravelDistanceDto> getTravelDistances(@PathVariable String id, @RequestParam("lat") Double lat, @RequestParam("lon") Double lon) {
        return travelDistanceApplicationService.requestTravelDistances(new OrganisationId(id), new GeoPoint(lat, lon));
    }

    static class SearchRequestDto {
        private List<QuestionAnswerDto> answers;
        private GeoPoint position;
        private double distance;

        public List<QuestionAnswerDto> getAnswers() {
            return answers;
        }

        public void setAnswers(List<QuestionAnswerDto> answers) {
            this.answers = answers;
        }

        public GeoPoint getPosition() {
            return position;
        }

        public void setPosition(GeoPoint position) {
            this.position = position;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }
    }

    static class BoundingBoxRequestDto {
        private GeoPoint position;
        private double distance;
        private BoundingBox boundingBox;

        public GeoPoint getPosition() {
            return position;
        }

        public void setPosition(GeoPoint position) {
            this.position = position;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }

        public BoundingBox getBoundingBox() {
            return boundingBox;
        }

        public void setBoundingBox(BoundingBox boundingBoxDto) {
            this.boundingBox = boundingBoxDto;
        }
    }

}
