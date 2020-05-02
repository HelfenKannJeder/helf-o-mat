package de.helfenkannjeder.helfomat.api;

import de.helfenkannjeder.helfomat.core.organization.Answer;
import de.helfenkannjeder.helfomat.core.organization.OrganizationType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Valentin Zickner
 */
@Component
@ConfigurationProperties(prefix = "helfomat")
@SuppressWarnings("unused")
public class QuestionConfiguration {

    private List<QuestionMapping> questions = new ArrayList<>();

    public List<QuestionMapping> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionMapping> questions) {
        this.questions = questions;
    }

    public static class QuestionMapping {
        private String id;

        private String question;

        private String description;

        private Answer defaultAnswer;

        private List<QuestionOrganizationGroupMapping> groups = new ArrayList<>();

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Answer getDefaultAnswer() {
            return defaultAnswer;
        }

        public void setDefaultAnswer(Answer defaultAnswer) {
            this.defaultAnswer = defaultAnswer;
        }

        public List<QuestionOrganizationGroupMapping> getGroups() {
            return groups;
        }

        public void setGroups(List<QuestionOrganizationGroupMapping> groups) {
            this.groups = groups;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public static class QuestionOrganizationGroupMapping {
            private OrganizationType organizationType;
            private String phrase;
            private Answer answer;

            public OrganizationType getOrganizationType() {
                return organizationType;
            }

            public void setOrganizationType(OrganizationType organizationType) {
                this.organizationType = organizationType;
            }

            public String getPhrase() {
                return phrase;
            }

            public void setPhrase(String phrase) {
                this.phrase = phrase;
            }

            public Answer getAnswer() {
                return answer;
            }

            public void setAnswer(Answer answer) {
                this.answer = answer;
            }
        }
    }


}
