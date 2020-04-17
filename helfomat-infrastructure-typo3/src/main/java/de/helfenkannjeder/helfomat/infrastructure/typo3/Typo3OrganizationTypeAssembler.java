package de.helfenkannjeder.helfomat.infrastructure.typo3;

import de.helfenkannjeder.helfomat.core.template.GroupTemplate;
import de.helfenkannjeder.helfomat.core.template.OrganizationTemplate;
import de.helfenkannjeder.helfomat.infrastructure.typo3.domain.TGroupTemplate;
import de.helfenkannjeder.helfomat.infrastructure.typo3.domain.TGroupTemplateCategory;
import de.helfenkannjeder.helfomat.infrastructure.typo3.domain.TOrganizationType;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Typo3OrganizationTypeAssembler {

    public static OrganizationTemplate toOrganizationTemplate(TOrganizationType organizationType) {
        return new OrganizationTemplate(
            organizationType.getName(),
            organizationType.getAcronym(),
            organizationType.isRegisterable(),
            toGroupTemplates(organizationType.getGroupTemplateCategories())
        );
    }

    private static List<GroupTemplate> toGroupTemplates(List<TGroupTemplateCategory> groupTemplateCategories) {
        return groupTemplateCategories
            .stream()
            .map(TGroupTemplateCategory::getGroupTemplates)
            .flatMap(Collection::stream)
            .map(Typo3OrganizationTypeAssembler::toGroupTemplate)
            .collect(Collectors.toList());
    }

    private static GroupTemplate toGroupTemplate(TGroupTemplate tGroupTemplate) {
        return new GroupTemplate(
            tGroupTemplate.getName(),
            tGroupTemplate.getSuggestion()
        );
    }

}
