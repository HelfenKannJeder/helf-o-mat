package de.helfenkannjeder.helfomat.api.organization.event;

import de.helfenkannjeder.helfomat.api.organization.ContactPersonDto;
import de.helfenkannjeder.helfomat.core.organization.OrganizationId;

/**
 * @author Valentin Zickner
 */
public class OrganizationEditDeleteContactPersonEventDto extends OrganizationEventDto {
    private ContactPersonDto contactPerson;

    OrganizationEditDeleteContactPersonEventDto() {
    }

    public OrganizationEditDeleteContactPersonEventDto(OrganizationId organizationId, ContactPersonDto contactPerson) {
        super(organizationId);
        this.contactPerson = contactPerson;
    }

    public ContactPersonDto getContactPerson() {
        return contactPerson;
    }

    @Override
    public <T> T visit(OrganizationEventDtoVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
