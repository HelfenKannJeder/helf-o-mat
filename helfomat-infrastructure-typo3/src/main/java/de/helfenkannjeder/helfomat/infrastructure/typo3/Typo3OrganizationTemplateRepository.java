package de.helfenkannjeder.helfomat.infrastructure.typo3;

import de.helfenkannjeder.helfomat.core.organisation.OrganisationType;
import de.helfenkannjeder.helfomat.core.template.OrganizationTemplate;
import de.helfenkannjeder.helfomat.core.template.OrganizationTemplateRepository;
import de.helfenkannjeder.helfomat.infrastructure.typo3.domain.TOrganisationType;
import org.springframework.stereotype.Repository;

/**
 * @author Valentin Zickner
 */
@Repository
public class Typo3OrganizationTemplateRepository implements OrganizationTemplateRepository {

    private Typo3OrganisationTypeRepository typo3OrganisationTypeRepository;

    public Typo3OrganizationTemplateRepository(Typo3OrganisationTypeRepository typo3OrganisationTypeRepository) {
        this.typo3OrganisationTypeRepository = typo3OrganisationTypeRepository;
    }

    @Override
    public OrganizationTemplate findByOrganizationType(OrganisationType organizationType) {
        TOrganisationType organisationType = this.typo3OrganisationTypeRepository.findByName(organizationType.getName());
        return Typo3OrganisationTypeAssembler.toOrganisationTemplate(organisationType);
    }

}
