package de.helfenkannjeder.helfomat.infrastructure.batch.processor;

import de.helfenkannjeder.helfomat.api.HelfomatConfiguration;
import de.helfenkannjeder.helfomat.core.organisation.Group;
import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationType;
import de.helfenkannjeder.helfomat.core.question.Answer;
import de.helfenkannjeder.helfomat.core.question.Question;
import de.helfenkannjeder.helfomat.core.question.QuestionId;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Valentin Zickner
 */
@Component
public class AnswerQuestionsProcessor implements ItemProcessor<Organisation, Organisation> {

    private final HelfomatConfiguration helfomatConfiguration;

    public AnswerQuestionsProcessor(HelfomatConfiguration helfomatConfiguration) {
        this.helfomatConfiguration = helfomatConfiguration;
    }

    @Override
    public Organisation process(Organisation organisation) throws Exception {
        if (organisation == null) {
            return null;
        }

        return new Organisation.Builder(organisation)
            .setQuestions(answerAllQuestionsForOrganisation(organisation))
            .build();
    }

    private List<Question> answerAllQuestionsForOrganisation(Organisation organisation) {
        return helfomatConfiguration.getQuestions()
            .stream()
            .map(questionMapping -> new Question(
                new QuestionId(questionMapping.getId()),
                questionMapping.getQuestion(),
                questionMapping.getPosition(),
                answerQuestionForOrganisation(questionMapping, organisation)
            ))
            .collect(Collectors.toList());
    }

    private Answer answerQuestionForOrganisation(HelfomatConfiguration.QuestionMapping question, Organisation organisation) {
        return question.getGroups()
            .stream()
            .filter(group -> organisationGroupConfigurationApplies(organisation.getOrganisationType(), organisation.getGroups(), group))
            .findFirst()
            .map(HelfomatConfiguration.QuestionMapping.QuestionOrganisationGroupMapping::getAnswer)
            .orElse(question.getDefaultAnswer());
    }

    private boolean organisationGroupConfigurationApplies(OrganisationType organisationType,
                                                          List<Group> groups,
                                                          HelfomatConfiguration.QuestionMapping.QuestionOrganisationGroupMapping questionMapping) {
        return hasSameOrganisationType(organisationType, questionMapping.getOrganisationType())
            && groupExistsWithPhrase(groups, questionMapping.getPhrase());
    }

    private boolean hasSameOrganisationType(OrganisationType organisationType, OrganisationType configurationOrganisationType) {
        return configurationOrganisationType == null || configurationOrganisationType.equals(organisationType);
    }

    private boolean groupExistsWithPhrase(List<Group> groups, String phrase) {
        return phrase == null || groups.stream().anyMatch(group -> group.getName().contains(phrase));
    }

}
