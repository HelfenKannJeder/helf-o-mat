package de.helfenkannjeder.helfomat.web.controller

import de.helfenkannjeder.helfomat.api.organization.OrganizationApplicationService
import de.helfenkannjeder.helfomat.api.organization.OrganizationSubmitEventDto
import de.helfenkannjeder.helfomat.api.organization.SearchSimilarOrganizationDto
import de.helfenkannjeder.helfomat.api.organization.ValidateWebsiteDto
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

/**
 * @author Valentin Zickner
 */
@RestController
@RequestMapping(path = ["/api"], produces = [MediaType.APPLICATION_JSON_VALUE])
class OrganizationEditController(private val organizationApplicationService: OrganizationApplicationService) {

    @PostMapping("/organization/search-similar")
    fun similarOrganizations(@RequestBody searchSimilarOrganizationDto: SearchSimilarOrganizationDto) =
        organizationApplicationService.findSimilarOrganizations(searchSimilarOrganizationDto)

    @PostMapping("/organization/validate-website")
    fun validateWebsite(@RequestBody validateWebsiteDto: ValidateWebsiteDto) =
        organizationApplicationService.validateWebsite(validateWebsiteDto)

    @PostMapping("/organization/submit")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun submitOrganization(@RequestBody organizationSubmitEventDto: OrganizationSubmitEventDto) =
        organizationApplicationService.submitOrganization(organizationSubmitEventDto)

}