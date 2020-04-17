package de.helfenkannjeder.helfomat.web.controller;

import de.helfenkannjeder.helfomat.api.organization.OrganizationApplicationService;
import de.helfenkannjeder.helfomat.api.organization.OrganizationSubmitEventDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Valentin Zickner
 */
@RestController
public class OrganizationEditController {

    private final OrganizationApplicationService organizationApplicationService;

    public OrganizationEditController(OrganizationApplicationService organizationApplicationService) {
        this.organizationApplicationService = organizationApplicationService;
    }

    @PostMapping("api/organization/submit")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void submitOrganization(@RequestBody OrganizationSubmitEventDto organizationSubmitEventDto) {
        this.organizationApplicationService.submitOrganization(organizationSubmitEventDto);
    }
}
