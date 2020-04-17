package de.helfenkannjeder.helfomat.api.template;

import de.helfenkannjeder.helfomat.core.organization.OrganizationType;
import de.helfenkannjeder.helfomat.core.template.OrganizationTemplateRepository;
import org.springframework.stereotype.Service;

@Service
public class OrganizationTemplateApplicationService {

    private OrganizationTemplateRepository organizationTemplateRepository;

    public OrganizationTemplateApplicationService(OrganizationTemplateRepository organizationTemplateRepository) {
        this.organizationTemplateRepository = organizationTemplateRepository;
    }

    public OrganizationTemplateDto findOrganizationTemplateByType(OrganizationType organizationType) {
        return OrganizationTemplateAssembler.toOrganizationTemplateDto(
            this.organizationTemplateRepository.findByOrganizationType(organizationType)
        );
    }

}
