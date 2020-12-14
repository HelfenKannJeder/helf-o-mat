package de.helfenkannjeder.helfomat.web.controller

import de.helfenkannjeder.helfomat.api.approval.AdminApplicationService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author Valentin Zickner
 */
@RestController
@RequestMapping(path = ["/api"], produces = [MediaType.APPLICATION_JSON_VALUE])
class AdminController(
    private val adminApplicationService: AdminApplicationService
) {

    @GetMapping("/reindex-all-organizations")
    fun reindexAllOrganizations() = adminApplicationService.reloadAllOrganizations()

}