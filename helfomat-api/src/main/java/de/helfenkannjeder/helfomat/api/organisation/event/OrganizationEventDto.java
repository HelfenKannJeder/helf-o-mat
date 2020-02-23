package de.helfenkannjeder.helfomat.api.organisation.event;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;

/**
 * @author Valentin Zickner
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
@JsonTypeInfo(
    use = JsonTypeInfo.Id.MINIMAL_CLASS,
    property = "type"
)
public abstract class OrganizationEventDto {
    private OrganisationId organisationId;

    protected OrganizationEventDto() {
    }

    public OrganizationEventDto(OrganisationId organisationId) {
        this.organisationId = organisationId;
    }

    public OrganisationId getOrganisationId() {
        return organisationId;
    }

}
