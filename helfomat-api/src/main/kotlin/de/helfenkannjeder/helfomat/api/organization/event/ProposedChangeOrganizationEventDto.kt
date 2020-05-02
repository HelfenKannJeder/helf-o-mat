package de.helfenkannjeder.helfomat.api.organization.event;

import de.helfenkannjeder.helfomat.core.organization.OrganizationId;

import java.util.List;

/**
 * @author Valentin Zickner
 */
public class ProposedChangeOrganizationEventDto extends OrganizationEventDto {

    private String author;
    private String sources;
    private List<OrganizationEventDto> changes;

    private ProposedChangeOrganizationEventDto() {
    }

    public ProposedChangeOrganizationEventDto(OrganizationId organizationId, String author, String sources, List<OrganizationEventDto> changes) {
        super(organizationId);
        this.author = author;
        this.sources = sources;
        this.changes = changes;
    }

    public String getAuthor() {
        return author;
    }

    public String getSources() {
        return sources;
    }

    public List<OrganizationEventDto> getChanges() {
        return changes;
    }

    @Override
    public <T> T visit(OrganizationEventDtoVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
