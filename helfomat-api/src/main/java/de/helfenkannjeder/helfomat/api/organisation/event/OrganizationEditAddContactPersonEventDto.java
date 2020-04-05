package de.helfenkannjeder.helfomat.api.organisation.event;

import de.helfenkannjeder.helfomat.api.organisation.ContactPersonDto;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;

/**
 * @author Valentin Zickner
 */
public class OrganizationEditAddContactPersonEventDto extends OrganizationEventDto {
    private int index;
    private ContactPersonDto contactPerson;

    OrganizationEditAddContactPersonEventDto() {
    }

    public OrganizationEditAddContactPersonEventDto(OrganisationId organisationId, int index, ContactPersonDto contactPerson) {
        super(organisationId);
        this.index = index;
        this.contactPerson = contactPerson;
    }

    public int getIndex() {
        return index;
    }

    public ContactPersonDto getContactPerson() {
        return contactPerson;
    }

    @Override
    public <T> T visit(OrganizationEventDtoVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
