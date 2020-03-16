package de.helfenkannjeder.helfomat.core.organisation.event;

import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;
import de.helfenkannjeder.helfomat.core.organisation.OrganizationEventVisitor;

import java.util.List;

/**
 * @author Valentin Zickner
 */
public class ProposedChangeOrganizationEvent extends OrganisationEvent {

    private String author;
    private String sources;
    private List<OrganisationEvent> changes;

    private ProposedChangeOrganizationEvent() {
    }

    public ProposedChangeOrganizationEvent(OrganisationId organisationId, String author, String sources, List<OrganisationEvent> changes) {
        super(organisationId);
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

    public List<OrganisationEvent> getChanges() {
        return changes;
    }

    @Override
    public Organisation.Builder applyOnOrganisationBuilder(Organisation.Builder organisation) {
        // this builder is doing nothing, since the change is only done during the approval.
        return organisation;
    }

    @Override
    public <T> T visit(OrganizationEventVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
