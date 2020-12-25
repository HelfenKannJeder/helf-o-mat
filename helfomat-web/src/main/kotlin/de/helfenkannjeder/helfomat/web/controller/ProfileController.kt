package de.helfenkannjeder.helfomat.web.controller

import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/api"], produces = [MediaType.APPLICATION_JSON_VALUE])
class ProfileController {

    @GetMapping("/profile/authorities")
    fun getProfile(authentication: Authentication) = authentication.authorities

}