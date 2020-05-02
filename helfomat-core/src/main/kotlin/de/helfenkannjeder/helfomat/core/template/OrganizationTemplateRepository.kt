package de.helfenkannjeder.helfomat.core.template;

import de.helfenkannjeder.helfomat.core.organization.OrganizationType;

/**
 * @author Valentin Zickner
 */
public interface OrganizationTemplateRepository {

    OrganizationTemplate findByOrganizationType(OrganizationType organizationType);

}
