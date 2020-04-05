package de.helfenkannjeder.helfomat.web.controller;

import de.helfenkannjeder.helfomat.api.template.OrganizationTemplateApplicationService;
import de.helfenkannjeder.helfomat.api.template.OrganizationTemplateDto;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationType;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrganizationTemplateController {

    private OrganizationTemplateApplicationService organizationTemplateApplicationService;

    public OrganizationTemplateController(OrganizationTemplateApplicationService organizationTemplateApplicationService) {
        this.organizationTemplateApplicationService = organizationTemplateApplicationService;
    }

    @GetMapping("/template/{organizationType}")
    public OrganizationTemplateDto getTemplateByOrganizationType(@PathVariable("organizationType") OrganisationType organizationType) {
        return this.organizationTemplateApplicationService.findOrganisationTemplateByType(organizationType);
    }

}
