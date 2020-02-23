package de.helfenkannjeder.helfomat.infrastructure.typo3;

import de.helfenkannjeder.helfomat.core.template.GroupTemplate;
import de.helfenkannjeder.helfomat.core.template.OrganizationTemplate;
import de.helfenkannjeder.helfomat.infrastructure.typo3.domain.TGroupTemplate;
import de.helfenkannjeder.helfomat.infrastructure.typo3.domain.TGroupTemplateCategory;
import de.helfenkannjeder.helfomat.infrastructure.typo3.domain.TOrganisationType;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Typo3OrganisationTypeAssembler {

    public static OrganizationTemplate toOrganisationTemplate(TOrganisationType organisationType) {
        return new OrganizationTemplate(
            organisationType.getName(),
            organisationType.getAcronym(),
            organisationType.isRegisterable(),
            toGroupTemplates(organisationType.getGroupTemplateCategories())
        );
    }

    private static List<GroupTemplate> toGroupTemplates(List<TGroupTemplateCategory> groupTemplateCategories) {
        return groupTemplateCategories
            .stream()
            .map(TGroupTemplateCategory::getGroupTemplates)
            .flatMap(Collection::stream)
            .map(Typo3OrganisationTypeAssembler::toGroupTemplate)
            .collect(Collectors.toList());
    }

    private static GroupTemplate toGroupTemplate(TGroupTemplate tGroupTemplate) {
        return new GroupTemplate(
            tGroupTemplate.getName(),
            tGroupTemplate.getSuggestion()
        );
    }

}
