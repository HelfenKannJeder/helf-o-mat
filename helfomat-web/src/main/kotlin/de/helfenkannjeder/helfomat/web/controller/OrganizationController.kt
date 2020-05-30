package de.helfenkannjeder.helfomat.web.controller

import de.helfenkannjeder.helfomat.api.organization.*
import de.helfenkannjeder.helfomat.core.geopoint.BoundingBox
import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint
import de.helfenkannjeder.helfomat.core.organization.OrganizationId
import de.helfenkannjeder.helfomat.core.organization.OrganizationType
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
    fun findGlobalOrganizations(): List<OrganizationDto> = organizationApplicationService.findGlobalOrganizations()

    @GetMapping("/organization/global/{organizationType}")
    fun getGlobalOrganizationByType(@PathVariable("organizationType") organizationType: OrganizationType) = organizationApplicationService.getGlobalOrganizationByType(organizationType)

    @PostMapping("/organization/global/byQuestionAnswers")
    fun findOrganizationsByQuestionAnswers(@RequestBody questionAnswersDto: List<QuestionAnswerDto>) = organizationApplicationService.findGlobalOrganizationsWith(questionAnswersDto)

    @GetMapping("/organization/byPosition")
    fun findOrganizationsByPosition(@RequestParam position: GeoPoint, @RequestParam distance: Double) = organizationApplicationService.findOrganizationsWith(position, distance)

    @PostMapping("/organization/byQuestionAnswersAndPosition")
    fun findOrganizationsByQuestionAnswersAndPosition(@RequestParam position: GeoPoint,
                                                      @RequestParam distance: Double,
                                                      @RequestBody questionAnswersDto: List<QuestionAnswerDto>) =
        organizationApplicationService.findOrganizationsWith(
            questionAnswersDto,
            position,
            distance
        )

    @PostMapping("/organization/boundingBox")
    fun boundingBox(@RequestBody searchRequestDto: BoundingBoxRequestDto): List<GeoPoint> = organizationApplicationService.findClusteredGeoPoints(
        searchRequestDto.position,
        searchRequestDto.distance,
        searchRequestDto.boundingBox
    )

    @GetMapping("/organization/{organizationName}")
    fun getOrganization(@PathVariable organizationName: String) = organizationApplicationService.findOrganizationDetails(organizationName)

    @GetMapping("/organization/{id}/travelDistances")
    fun getTravelDistances(@PathVariable id: String,
                           @RequestParam("lat") lat: Double,
                           @RequestParam("lon") lon: Double) =
        travelDistanceApplicationService.requestTravelDistances(OrganizationId(id), GeoPoint(lat, lon))

    @PostMapping("/organization/compare")
    fun compareOrganizations(@RequestBody compareOrganizationDto: CompareOrganizationDto) =
        organizationApplicationService.compareOrganizations(compareOrganizationDto)

    @GetMapping("/organization/types")
    fun getOrganizationTypes() = organizationApplicationService.findOrganizationTypes()

    data class BoundingBoxRequestDto(
        var position: GeoPoint?,
        var distance: Double = 0.0,
        var boundingBox: BoundingBox
    )
}
