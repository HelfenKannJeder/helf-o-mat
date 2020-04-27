package de.helfenkannjeder.helfomat.web.controller;

import de.helfenkannjeder.helfomat.api.geopoint.TravelDistanceDto;
import de.helfenkannjeder.helfomat.api.organization.CompareOrganizationDto;
import de.helfenkannjeder.helfomat.api.organization.OrganizationApplicationService;
import de.helfenkannjeder.helfomat.api.organization.OrganizationDetailDto;
import de.helfenkannjeder.helfomat.api.organization.OrganizationDto;
import de.helfenkannjeder.helfomat.api.organization.QuestionAnswerDto;
import de.helfenkannjeder.helfomat.api.organization.TravelDistanceApplicationService;
import de.helfenkannjeder.helfomat.api.organization.event.OrganizationEventDto;
import de.helfenkannjeder.helfomat.core.geopoint.BoundingBox;
import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint;
import de.helfenkannjeder.helfomat.core.organization.OrganizationId;
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
public class OrganizationController {

    private final OrganizationApplicationService organizationApplicationService;
    private final TravelDistanceApplicationService travelDistanceApplicationService;

    @Autowired
    public OrganizationController(OrganizationApplicationService organizationApplicationService, TravelDistanceApplicationService
        travelDistanceApplicationService) {
        this.organizationApplicationService = organizationApplicationService;
        this.travelDistanceApplicationService = travelDistanceApplicationService;
    }

    @GetMapping("/organization/global")
    public List<OrganizationDto> findGlobalOrganizations() {
        return this.organizationApplicationService.findGlobalOrganizations();
    }

    @PostMapping("/organization/global/byQuestionAnswers")
    public List<OrganizationDto> findOrganizationsByQuestionAnswers(@RequestBody List<QuestionAnswerDto> questionAnswersDto) {
        return this.organizationApplicationService.findGlobalOrganizationsWith(questionAnswersDto);
    }

    @GetMapping("/organization/byPosition")
    public List<OrganizationDto> findOrganizationsByPosition(@RequestParam GeoPoint position, @RequestParam double distance) {
        return this.organizationApplicationService.findOrganizationsWith(position, distance);
    }

    @PostMapping("/organization/byQuestionAnswersAndPosition")
    public List<OrganizationDto> findOrganizationsByQuestionAnswersAndPosition(@RequestParam GeoPoint position, @RequestParam double distance, @RequestBody List<QuestionAnswerDto> questionAnswersDto) {
        return this.organizationApplicationService.findOrganizationsWith(
            questionAnswersDto,
            position,
            distance
        );
    }

    @PostMapping("/organization/boundingBox")
    public List<GeoPoint> boundingBox(@RequestBody BoundingBoxRequestDto searchRequestDto) {
        return organizationApplicationService.findClusteredGeoPoints(
            searchRequestDto.getPosition(),
            searchRequestDto.getDistance(),
            searchRequestDto.getBoundingBox()
        );
    }

    @GetMapping("/organization/{organizationName}")
    public OrganizationDetailDto getOrganization(@PathVariable String organizationName) {
        return this.organizationApplicationService.findOrganizationDetails(organizationName);
    }

    @GetMapping("/organization/{id}/travelDistances")
    public List<TravelDistanceDto> getTravelDistances(@PathVariable String id, @RequestParam("lat") Double lat, @RequestParam("lon") Double lon) {
        return travelDistanceApplicationService.requestTravelDistances(new OrganizationId(id), new GeoPoint(lat, lon));
    }

    @PostMapping("/organization/compare")
    public List<OrganizationEventDto> compareOrganizations(@RequestBody CompareOrganizationDto compareOrganizationDto) {
        return this.organizationApplicationService.compareOrganizations(compareOrganizationDto);
    }

    @SuppressWarnings({"WeakerAccess", "CanBeFinal", "unused"})
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

    @SuppressWarnings({"WeakerAccess", "CanBeFinal", "unused"})
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
