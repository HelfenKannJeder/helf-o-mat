package de.helfenkannjeder.helfomat.web.controller;

import de.helfenkannjeder.helfomat.api.organization.OrganizationApplicationService;
import de.helfenkannjeder.helfomat.api.organization.OrganizationDetailDto;
import de.helfenkannjeder.helfomat.api.organization.OrganizationSubmitEventDto;
import de.helfenkannjeder.helfomat.api.organization.SearchSimilarOrganizationDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Valentin Zickner
 */
@RestController
public class OrganizationEditController {

    private final OrganizationApplicationService organizationApplicationService;

    public OrganizationEditController(OrganizationApplicationService organizationApplicationService) {
        this.organizationApplicationService = organizationApplicationService;
    }

    @PostMapping("api/organization/search-similar")
    public List<OrganizationDetailDto> similarOrganizations(@RequestBody SearchSimilarOrganizationDto searchSimilarOrganizationDto) {
        return this.organizationApplicationService.findSimilarOrganizations(searchSimilarOrganizationDto);
    }


    @PostMapping("api/organization/submit")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void submitOrganization(@RequestBody OrganizationSubmitEventDto organizationSubmitEventDto) {
        this.organizationApplicationService.submitOrganization(organizationSubmitEventDto);
    }
}
