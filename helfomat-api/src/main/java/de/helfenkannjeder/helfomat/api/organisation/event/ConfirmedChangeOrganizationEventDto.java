package de.helfenkannjeder.helfomat.api.organisation.event;

import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;

import java.util.List;

/**
 * @author Valentin Zickner
 */
public class ConfirmedChangeOrganizationEventDto extends OrganizationEventDto {

    private String approvedBy;
    private String author;
    private String sources;
    private List<OrganizationEventDto> changes;

    private ConfirmedChangeOrganizationEventDto() {
    }

    public ConfirmedChangeOrganizationEventDto(OrganisationId organisationId, String approvedBy, String author, String sources, List<OrganizationEventDto> changes) {
        super(organisationId);
        this.approvedBy = approvedBy;
        this.author = author;
        this.sources = sources;
        this.changes = changes;
    }

    public String getApprovedBy() {
        return approvedBy;
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
