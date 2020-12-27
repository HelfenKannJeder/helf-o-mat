package de.helfenkannjeder.helfomat.api.contact

import de.helfenkannjeder.helfomat.core.contact.ContactRequest
import de.helfenkannjeder.helfomat.core.contact.ContactRequestContactPerson
import de.helfenkannjeder.helfomat.core.contact.ContactRequestId
import de.helfenkannjeder.helfomat.core.organization.ContactPerson
import de.helfenkannjeder.helfomat.core.organization.Organization

fun CreateContactRequestDto.toContactRequest(confirmationCode: String, organization: Organization) = ContactRequest(
    ContactRequestId(),
    this.name,
    this.email,
    this.subject,
    this.message,
    confirmationCode,
    organizationId = organization.id,
    contactPerson = organization.contactPersons[this.organizationContactPersonIndex].toContactRequestContactPerson()
)

fun ContactPerson.toContactRequestContactPerson() = ContactRequestContactPerson(
    this.firstname,
    this.lastname,
    this.mail ?: throw ContactRequestInvalid(),
    this.telephone
)