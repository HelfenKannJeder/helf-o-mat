package de.helfenkannjeder.helfomat.api.organisation.event;

import de.helfenkannjeder.helfomat.api.organisation.ContactPersonDto;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;

/**
 * @author Valentin Zickner
 */
public class OrganizationEditDeleteContactPersonEventDto extends OrganizationEventDto {
    private ContactPersonDto contactPerson;

    OrganizationEditDeleteContactPersonEventDto() {
    }

    public OrganizationEditDeleteContactPersonEventDto(OrganisationId organisationId, ContactPersonDto contactPerson) {
        super(organisationId);
        this.contactPerson = contactPerson;
    }

    public ContactPersonDto getContactPerson() {
        return contactPerson;
    }
}
