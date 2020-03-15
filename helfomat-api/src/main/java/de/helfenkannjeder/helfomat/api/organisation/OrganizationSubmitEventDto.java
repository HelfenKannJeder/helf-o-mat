package de.helfenkannjeder.helfomat.api.organisation;

import de.helfenkannjeder.helfomat.api.organisation.event.OrganizationEventDto;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;

import java.util.List;

/**
 * @author Valentin Zickner
 */
public class OrganizationSubmitEventDto {

    private OrganisationId organizationId;
    private String sources;
    private List<OrganizationEventDto> events;

    private OrganizationSubmitEventDto() {
    }

    public OrganizationSubmitEventDto(OrganisationId organizationId, String sources, List<OrganizationEventDto> events) {
        this.organizationId = organizationId;
        this.sources = sources;
        this.events = events;
    }

    public OrganisationId getOrganizationId() {
        return organizationId;
    }

    public String getSources() {
        return sources;
    }

    public List<OrganizationEventDto> getEvents() {
        return events;
    }

}
