package de.helfenkannjeder.helfomat.infrastructure.typo3;

import de.helfenkannjeder.helfomat.core.organization.OrganizationType;
import de.helfenkannjeder.helfomat.core.template.OrganizationTemplate;
import de.helfenkannjeder.helfomat.core.template.OrganizationTemplateRepository;
import de.helfenkannjeder.helfomat.infrastructure.typo3.domain.TOrganizationType;
import org.springframework.stereotype.Repository;

/**
 * @author Valentin Zickner
 */
@Repository
public class Typo3OrganizationTemplateRepository implements OrganizationTemplateRepository {

    private Typo3OrganizationTypeRepository typo3OrganizationTypeRepository;

    public Typo3OrganizationTemplateRepository(Typo3OrganizationTypeRepository typo3OrganizationTypeRepository) {
        this.typo3OrganizationTypeRepository = typo3OrganizationTypeRepository;
    }

    @Override
    public OrganizationTemplate findByOrganizationType(OrganizationType organizationType) {
        TOrganizationType tOrganizationType = this.typo3OrganizationTypeRepository.findByName(organizationType.getName());
        return Typo3OrganizationTypeAssembler.toOrganizationTemplate(tOrganizationType);
    }

}
