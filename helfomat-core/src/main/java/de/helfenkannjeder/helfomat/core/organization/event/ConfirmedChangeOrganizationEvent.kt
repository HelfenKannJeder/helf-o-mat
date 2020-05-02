package de.helfenkannjeder.helfomat.core.organization.event;

import de.helfenkannjeder.helfomat.core.organization.Organization;
import de.helfenkannjeder.helfomat.core.organization.OrganizationEventVisitor;
import de.helfenkannjeder.helfomat.core.organization.OrganizationId;

import java.util.List;

/**
 * @author Valentin Zickner
 */
public class ConfirmedChangeOrganizationEvent extends OrganizationEvent {

    private String approvedBy;
    private String author;
    private String sources;
    private List<OrganizationEvent> changes;

    private ConfirmedChangeOrganizationEvent() {
    }

    public ConfirmedChangeOrganizationEvent(OrganizationId organizationId, String approvedBy, String author, String sources, List<OrganizationEvent> changes) {
        super(organizationId);
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

    public List<OrganizationEvent> getChanges() {
        return changes;
    }

    @Override
    public Organization.Builder applyOnOrganizationBuilder(Organization.Builder organization) {
        if (changes != null) {
            for (OrganizationEvent change : changes) {
                change.applyOnOrganizationBuilder(organization);
            }
        }
        return organization;
    }

    @Override
    public <T> T visit(OrganizationEventVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "ConfirmedChangeOrganizationEvent{" +
            "approvedBy='" + approvedBy + '\'' +
            ", author='" + author + '\'' +
            ", sources='" + sources + '\'' +
            ", changes=" + changes +
            ", organizationId=" + organizationId +
            '}';
    }
}
