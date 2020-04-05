package de.helfenkannjeder.helfomat.core.organisation.event;

import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;
import de.helfenkannjeder.helfomat.core.organisation.OrganizationEventVisitor;

/**
 * @author Valentin Zickner
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class OrganisationEditUrlNameEvent extends OrganisationEditEvent {
    private String urlName;

    protected OrganisationEditUrlNameEvent() {
    }

    public OrganisationEditUrlNameEvent(OrganisationId organisationId, String urlName) {
        super(organisationId);
        this.urlName = urlName;
    }

    public String getUrlName() {
        return urlName;
    }

    @Override
    public Organisation.Builder applyOnOrganisationBuilder(Organisation.Builder organisation) {
        return organisation.setUrlName(urlName);
    }

    @Override
    public <T> T visit(OrganizationEventVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
