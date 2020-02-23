package de.helfenkannjeder.helfomat.core.template;

import de.helfenkannjeder.helfomat.core.organisation.OrganisationType;

/**
 * @author Valentin Zickner
 */
public interface OrganizationTemplateRepository {

    OrganizationTemplate findByOrganizationType(OrganisationType organizationType);

}
