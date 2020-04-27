package de.helfenkannjeder.helfomat.web.controller

import de.helfenkannjeder.helfomat.api.geopoint.TravelDistanceDto
import de.helfenkannjeder.helfomat.api.organization.*
import de.helfenkannjeder.helfomat.api.organization.event.OrganizationEventDto
import de.helfenkannjeder.helfomat.core.geopoint.BoundingBox
import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint
import de.helfenkannjeder.helfomat.core.organization.OrganizationId
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

/**
 * @author Valentin Zickner
 */
@RestController
@RequestMapping(path = ["/api"], produces = [MediaType.APPLICATION_JSON_VALUE])
class OrganizationController(
    private val organizationApplicationService: OrganizationApplicationService,
    private val travelDistanceApplicationService: TravelDistanceApplicationService) {

    @GetMapping("/organization/global")
    fun findGlobalOrganizations(): List<OrganizationDto> {
        return organizationApplicationService.findGlobalOrganizations()
    }

    @PostMapping("/organization/global/byQuestionAnswers")
    fun findOrganizationsByQuestionAnswers(@RequestBody questionAnswersDto: List<QuestionAnswerDto>): List<OrganizationDto> {
        return organizationApplicationService.findGlobalOrganizationsWith(questionAnswersDto)
    }

    @GetMapping("/organization/byPosition")
    fun findOrganizationsByPosition(@RequestParam position: GeoPoint, @RequestParam distance: Double): List<OrganizationDto> {
        return organizationApplicationService.findOrganizationsWith(position, distance)
    }

    @PostMapping("/organization/byQuestionAnswersAndPosition")
    fun findOrganizationsByQuestionAnswersAndPosition(@RequestParam position: GeoPoint,
                                                      @RequestParam distance: Double,
                                                      @RequestBody questionAnswersDto: List<QuestionAnswerDto>): List<OrganizationDto> {
        return organizationApplicationService.findOrganizationsWith(
            questionAnswersDto,
            position,
            distance
        )
    }

    @PostMapping("/organization/boundingBox")
    fun boundingBox(@RequestBody searchRequestDto: BoundingBoxRequestDto): List<GeoPoint> {
        return organizationApplicationService.findClusteredGeoPoints(
            searchRequestDto.position,
            searchRequestDto.distance,
            searchRequestDto.boundingBox
        )
    }

    @GetMapping("/organization/{organizationName}")
    fun getOrganization(@PathVariable organizationName: String?): OrganizationDetailDto {
        return organizationApplicationService.findOrganizationDetails(organizationName!!)
    }

    @GetMapping("/organization/{id}/travelDistances")
    fun getTravelDistances(@PathVariable id: String,
                           @RequestParam("lat") lat: Double,
                           @RequestParam("lon") lon: Double): List<TravelDistanceDto> {
        return travelDistanceApplicationService.requestTravelDistances(OrganizationId(id), GeoPoint(lat, lon))
    }

    @PostMapping("/organization/compare")
    fun compareOrganizations(@RequestBody compareOrganizationDto: CompareOrganizationDto): List<OrganizationEventDto> {
        return organizationApplicationService.compareOrganizations(compareOrganizationDto)
    }

}

data class BoundingBoxRequestDto (
    var position: GeoPoint?,
    var distance: Double = 0.0,
    var boundingBox: BoundingBox
)