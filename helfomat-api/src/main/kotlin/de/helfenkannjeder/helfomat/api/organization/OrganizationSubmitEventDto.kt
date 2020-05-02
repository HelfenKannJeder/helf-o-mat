package de.helfenkannjeder.helfomat.api.organization;

import de.helfenkannjeder.helfomat.api.organization.event.OrganizationEventDto;
import de.helfenkannjeder.helfomat.core.organization.OrganizationId;

import java.util.List;

/**
 * @author Valentin Zickner
 */
public class OrganizationSubmitEventDto {

    private OrganizationId organizationId;
    private String sources;
    private List<OrganizationEventDto> events;

    private OrganizationSubmitEventDto() {
    }

    public OrganizationSubmitEventDto(OrganizationId organizationId, String sources, List<OrganizationEventDto> events) {
        this.organizationId = organizationId;
        this.sources = sources;
        this.events = events;
    }

    public OrganizationId getOrganizationId() {
        return organizationId;
    }

    public String getSources() {
        return sources;
    }

    public List<OrganizationEventDto> getEvents() {
        return events;
    }

}
