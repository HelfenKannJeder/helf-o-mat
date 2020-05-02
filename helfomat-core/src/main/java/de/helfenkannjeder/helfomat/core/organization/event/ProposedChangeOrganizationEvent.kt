package de.helfenkannjeder.helfomat.core.organization.event;

import de.helfenkannjeder.helfomat.core.organization.Organization;
import de.helfenkannjeder.helfomat.core.organization.OrganizationEventVisitor;
import de.helfenkannjeder.helfomat.core.organization.OrganizationId;

import java.util.List;

/**
 * @author Valentin Zickner
 */
public class ProposedChangeOrganizationEvent extends OrganizationEvent {

    private String author;
    private String sources;
    private List<OrganizationEvent> changes;

    private ProposedChangeOrganizationEvent() {
    }

    public ProposedChangeOrganizationEvent(OrganizationId organizationId, String author, String sources, List<OrganizationEvent> changes) {
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

    public List<OrganizationEvent> getChanges() {
        return changes;
    }

    @Override
    public Organization.Builder applyOnOrganizationBuilder(Organization.Builder organization) {
        // this builder is doing nothing, since the change is only done during the approval.
        return organization;
    }

    @Override
    public <T> T visit(OrganizationEventVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "ProposedChangeOrganizationEvent{" +
            "author='" + author + '\'' +
            ", sources='" + sources + '\'' +
            ", changes=" + changes +
            ", organizationId=" + organizationId +
            '}';
    }
}
