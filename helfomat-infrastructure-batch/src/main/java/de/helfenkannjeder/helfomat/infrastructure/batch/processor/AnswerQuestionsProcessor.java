package de.helfenkannjeder.helfomat.infrastructure.batch.processor;

import de.helfenkannjeder.helfomat.api.HelfomatConfiguration;
import de.helfenkannjeder.helfomat.core.organization.Answer;
import de.helfenkannjeder.helfomat.core.organization.Group;
import de.helfenkannjeder.helfomat.core.organization.Organization;
import de.helfenkannjeder.helfomat.core.organization.OrganizationType;
import de.helfenkannjeder.helfomat.core.organization.QuestionAnswer;
import de.helfenkannjeder.helfomat.core.question.QuestionId;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Valentin Zickner
 */
@Component
public class AnswerQuestionsProcessor implements ItemProcessor<Organization, Organization> {

    private final HelfomatConfiguration helfomatConfiguration;

    public AnswerQuestionsProcessor(HelfomatConfiguration helfomatConfiguration) {
        this.helfomatConfiguration = helfomatConfiguration;
    }

    @Override
    public Organization process(Organization organization) {
        if (organization == null) {
            return null;
        }

        return new Organization.Builder(organization)
            .setQuestionAnswers(answerAllQuestionsForOrganization(organization))
            .build();
    }

    private List<QuestionAnswer> answerAllQuestionsForOrganization(Organization organization) {
        return helfomatConfiguration.getQuestions()
            .stream()
            .map(questionMapping -> new QuestionAnswer(
                new QuestionId(questionMapping.getId()),
                answerQuestionForOrganization(questionMapping, organization)
            ))
            .collect(Collectors.toList());
    }

    private Answer answerQuestionForOrganization(HelfomatConfiguration.QuestionMapping question, Organization organization) {
        return question.getGroups()
            .stream()
            .filter(group -> organizationGroupConfigurationApplies(organization.getOrganizationType(), organization.getGroups(), group))
            .findFirst()
            .map(HelfomatConfiguration.QuestionMapping.QuestionOrganizationGroupMapping::getAnswer)
            .orElse(question.getDefaultAnswer());
    }

    private boolean organizationGroupConfigurationApplies(OrganizationType organizationType,
                                                          List<Group> groups,
                                                          HelfomatConfiguration.QuestionMapping.QuestionOrganizationGroupMapping questionMapping) {
        return hasSameOrganizationType(organizationType, questionMapping.getOrganizationType())
            && groupExistsWithPhrase(groups, questionMapping.getPhrase());
    }

    private boolean hasSameOrganizationType(OrganizationType organizationType, OrganizationType configurationOrganizationType) {
        return configurationOrganizationType == null || configurationOrganizationType.equals(organizationType);
    }

    private boolean groupExistsWithPhrase(List<Group> groups, String phrase) {
        return phrase == null || groups.stream().anyMatch(group -> group.getName().contains(phrase));
    }

}
