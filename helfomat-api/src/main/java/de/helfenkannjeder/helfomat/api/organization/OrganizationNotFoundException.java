package de.helfenkannjeder.helfomat.api.organization;

import de.helfenkannjeder.helfomat.core.organization.OrganizationId;

/**
 * @author Valentin Zickner
 */
public class OrganizationNotFoundException extends RuntimeException {
    public OrganizationNotFoundException(OrganizationId organizationId) {
        super("Organization with '" + organizationId.getValue() + "' could not be found.");
    }
}
