package de.helfenkannjeder.helfomat.api.organization.event;

import de.helfenkannjeder.helfomat.api.organization.ContactPersonDto;
import de.helfenkannjeder.helfomat.core.organization.OrganizationId;

/**
 * @author Valentin Zickner
 */
public class OrganizationEditAddContactPersonEventDto extends OrganizationEventDto {
    private int index;
    private ContactPersonDto contactPerson;

    OrganizationEditAddContactPersonEventDto() {
    }

    public OrganizationEditAddContactPersonEventDto(OrganizationId organizationId, int index, ContactPersonDto contactPerson) {
        super(organizationId);
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
