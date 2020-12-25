package de.helfenkannjeder.helfomat.web.controller

import de.helfenkannjeder.helfomat.api.contact.ContactApplicationService
import de.helfenkannjeder.helfomat.api.contact.CreateContactRequestDto
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author Valentin Zickner
 */
@RestController
@RequestMapping(path = ["/api"], produces = [MediaType.APPLICATION_JSON_VALUE])
class ContactController(
    private val contactApplicationService: ContactApplicationService
) {

    @PostMapping("/contact/request")
    fun createContactRequest(@RequestBody contactRequestDto: CreateContactRequestDto) = contactApplicationService.createContactRequest(contactRequestDto)
}