package de.helfenkannjeder.helfomat.api.contact

import org.springframework.stereotype.Service

@Service
open class ContactApplicationService {

    fun createContactRequest(contactRequestDto: CreateContactRequestDto) {
        println("Contact request: $contactRequestDto")
    }

}