package de.helfenkannjeder.helfomat.api.contact

import de.helfenkannjeder.helfomat.core.contact.ContactRequestId

/**
 * @author Valentin Zickner
 */
data class ConfirmContactRequestDto(
   val contactRequestId: ContactRequestId,
   val confirmationCode: String
)