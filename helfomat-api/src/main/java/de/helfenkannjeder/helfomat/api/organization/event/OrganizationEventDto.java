package de.helfenkannjeder.helfomat.api.organization.event;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.helfenkannjeder.helfomat.core.organization.OrganizationId;

/**
 * @author Valentin Zickner
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
@JsonTypeInfo(
    use = JsonTypeInfo.Id.MINIMAL_CLASS,
    property = "type"
)
public abstract class OrganizationEventDto {
    private OrganizationId organizationId;

    protected OrganizationEventDto() {
    }

    public OrganizationEventDto(OrganizationId organizationId) {
        this.organizationId = organizationId;
    }

    public OrganizationId getOrganizationId() {
        return organizationId;
    }

    public abstract <T> T visit(OrganizationEventDtoVisitor<T> visitor);

}
