package de.helfenkannjeder.helfomat.configuration;

import de.helfenkannjeder.helfomat.domain.Question;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Valentin Zickner
 */
@Component
@ConfigurationProperties(prefix = "helfomat")
public class HelfomatConfiguration {
    List<QuestionMapping> questions = new ArrayList<>();

    public List<QuestionMapping> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionMapping> questions) {
        this.questions = questions;
    }

    public static class QuestionMapping {
        private long uid;

        private String question;

        private Question.Answer defaultAnswer;

        private List<GroupMapping> groups = new ArrayList<>();

        private int position;

        public long getUid() {
            return uid;
        }

        public void setUid(long uid) {
            this.uid = uid;
        }

        public Question.Answer getDefaultAnswer() {
            return defaultAnswer;
        }

        public void setDefaultAnswer(Question.Answer defaultAnswer) {
            this.defaultAnswer = defaultAnswer;
        }

        public List<GroupMapping> getGroups() {
            return groups;
        }

        public void setGroups(List<GroupMapping> groups) {
            this.groups = groups;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public static class GroupMapping {
            private String phrase;
            private Question.Answer answer;

            public String getPhrase() {
                return phrase;
            }

            public void setPhrase(String phrase) {
                this.phrase = phrase;
            }

            public Question.Answer getAnswer() {
                return answer;
            }

            public void setAnswer(Question.Answer answer) {
                this.answer = answer;
            }
        }
    }

}
