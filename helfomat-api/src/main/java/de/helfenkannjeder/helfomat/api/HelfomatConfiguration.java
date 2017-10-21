package de.helfenkannjeder.helfomat.api;

import de.helfekannjeder.helfomat.core.organisation.Question;
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

    private static final String DEFAULT_PICTURE_FOLDER = "pictures";

    private List<QuestionMapping> questions = new ArrayList<>();

    private String pictureFolder = DEFAULT_PICTURE_FOLDER;

    private List<PictureSize> pictureSizes = new ArrayList<>();

    public List<QuestionMapping> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionMapping> questions) {
        this.questions = questions;
    }

    public String getPictureFolder() {
        return pictureFolder;
    }

    public void setPictureFolder(String pictureFolder) {
        this.pictureFolder = pictureFolder;
    }

    public List<PictureSize> getPictureSizes() {
        return pictureSizes;
    }

    public void setPictureSizes(List<PictureSize> pictureSizes) {
        this.pictureSizes = pictureSizes;
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

    public static class PictureSize {
        private String name;
        private Integer width;
        private Integer height;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getWidth() {
            return width;
        }

        public void setWidth(Integer width) {
            this.width = width;
        }

        public Integer getHeight() {
            return height;
        }

        public void setHeight(Integer height) {
            this.height = height;
        }
    }

}
