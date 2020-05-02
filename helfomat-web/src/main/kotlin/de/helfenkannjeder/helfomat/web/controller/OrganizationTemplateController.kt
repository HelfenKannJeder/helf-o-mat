package de.helfenkannjeder.helfomat.web.controller

import de.helfenkannjeder.helfomat.api.template.OrganizationTemplateApplicationService
import de.helfenkannjeder.helfomat.core.organization.OrganizationType
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/api"], produces = [MediaType.APPLICATION_JSON_VALUE])
class OrganizationTemplateController(private val organizationTemplateApplicationService: OrganizationTemplateApplicationService) {

    @GetMapping("/template/{organizationType}")
    fun getTemplateByOrganizationType(@PathVariable("organizationType") organizationType: OrganizationType) =
        organizationTemplateApplicationService.findOrganizationTemplateByType(organizationType)

}