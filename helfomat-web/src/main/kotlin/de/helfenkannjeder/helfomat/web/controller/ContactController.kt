package de.helfenkannjeder.helfomat.web.controller

import de.helfenkannjeder.helfomat.api.contact.ConfirmContactRequestDto
import de.helfenkannjeder.helfomat.api.contact.ContactApplicationService
import de.helfenkannjeder.helfomat.api.contact.CreateContactRequestDto
import de.helfenkannjeder.helfomat.api.contact.ResendContactRequestDto
import de.helfenkannjeder.helfomat.core.contact.ContactRequestId
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

/**
 * @author Valentin Zickner
 */
@RestController
@RequestMapping(path = ["/api"], produces = [MediaType.APPLICATION_JSON_VALUE])
class ContactController(
    private val contactApplicationService: ContactApplicationService
) {

    @GetMapping("/contact/{contactRequestId}")
    fun getContactRequest(@PathVariable("contactRequestId") contactRequestId: ContactRequestId) = contactApplicationService.getById(contactRequestId)

    @PostMapping("/contact/request")
    fun createContactRequest(@RequestBody contactRequestDto: CreateContactRequestDto) = contactApplicationService.createContactRequest(contactRequestDto)

    @PostMapping("/contact/resend")
    fun resendContactRequest(@RequestBody resendContactRequestDto: ResendContactRequestDto) = contactApplicationService.resendContactRequest(resendContactRequestDto)

    @PostMapping("/contact/confirm")
    fun confirmContactRequest(@RequestBody confirmContactRequestDto: ConfirmContactRequestDto) = contactApplicationService.confirmContactRequest(confirmContactRequestDto)
}