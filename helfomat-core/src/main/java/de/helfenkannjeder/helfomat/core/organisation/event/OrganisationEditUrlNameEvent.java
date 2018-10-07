package de.helfenkannjeder.helfomat.core.organisation.event;

import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;

/**
 * @author Valentin Zickner
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class OrganisationEditUrlNameEvent extends OrganisationEditEvent {
    private String urlName;

    public OrganisationEditUrlNameEvent(OrganisationId organisationId, String urlName) {
        super(organisationId);
        this.urlName = urlName;
    }

    @Override
    public Organisation.Builder applyOnOrganisationBuilder(Organisation.Builder organisation) {
        return organisation.setUrlName(urlName);
    }
}
