package de.helfenkannjeder.helfomat.core.organisation.event;

import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;

/**
 * @author Valentin Zickner
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal", "unused"})
public class OrganisationEditDescriptionEvent extends OrganisationEditEvent {
    private String description;

    protected OrganisationEditDescriptionEvent() {
    }

    public OrganisationEditDescriptionEvent(OrganisationId organisationId, String description) {
        super(organisationId);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public Organisation.Builder applyOnOrganisationBuilder(Organisation.Builder organisation) {
        return organisation.setDescription(description);
    }
}
