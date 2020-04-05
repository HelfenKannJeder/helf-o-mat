package de.helfenkannjeder.helfomat.core.organisation.event;

import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;
import de.helfenkannjeder.helfomat.core.organisation.OrganizationEventVisitor;

import java.util.List;

/**
 * @author Valentin Zickner
 */
public class ConfirmedChangeOrganizationEvent extends OrganisationEvent {

    private String approvedBy;
    private String author;
    private String sources;
    private List<OrganisationEvent> changes;

    private ConfirmedChangeOrganizationEvent() {
    }

    public ConfirmedChangeOrganizationEvent(OrganisationId organisationId, String approvedBy, String author, String sources, List<OrganisationEvent> changes) {
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

    public List<OrganisationEvent> getChanges() {
        return changes;
    }

    @Override
    public Organisation.Builder applyOnOrganisationBuilder(Organisation.Builder organisation) {
        if (changes != null) {
            for (OrganisationEvent change : changes) {
                change.applyOnOrganisationBuilder(organisation);
            }
        }
        return organisation;
    }

    @Override
    public <T> T visit(OrganizationEventVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
