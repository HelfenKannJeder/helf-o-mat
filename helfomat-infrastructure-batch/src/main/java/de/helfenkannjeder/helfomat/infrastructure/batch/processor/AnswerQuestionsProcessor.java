package de.helfenkannjeder.helfomat.infrastructure.batch.processor;

import de.helfenkannjeder.helfomat.api.HelfomatConfiguration;
import de.helfenkannjeder.helfomat.core.organisation.Group;
import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.question.Question;
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
            .setQuestions(extractQuestions(organisation.getGroups()))
            .build();
    }

    private List<Question> extractQuestions(List<Group> groups) {
        return helfomatConfiguration.getQuestions().stream()
            .map(questionMapping -> {
                Question question = new Question(
                    questionMapping.getUid(),
                    questionMapping.getQuestion(),
                    questionMapping.getPosition(),
                    questionMapping.getDefaultAnswer()
                );

                for (HelfomatConfiguration.QuestionMapping.GroupMapping group : questionMapping.getGroups()) {
                    if (groupExistsWithPhrase(groups, group.getPhrase())) {
                        question.setAnswer(group.getAnswer());
                    }
                }

                return question;
            })
            .collect(Collectors.toList());
    }

    private boolean groupExistsWithPhrase(List<Group> groups, String phrase) {
        return groups.stream().anyMatch(group -> group.getName().contains(phrase));
    }

}
