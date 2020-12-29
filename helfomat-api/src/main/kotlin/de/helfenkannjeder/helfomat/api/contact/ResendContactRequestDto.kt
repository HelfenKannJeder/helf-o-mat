package de.helfenkannjeder.helfomat.api.contact

import de.helfenkannjeder.helfomat.core.contact.ContactRequestId

data class ResendContactRequestDto(
    val contactRequestId: ContactRequestId,
    val captcha: String
)