package de.helfenkannjeder.helfomat.api;

import de.helfenkannjeder.helfomat.core.organisation.Answer;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationType;
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

    private AutoImport autoImport = new AutoImport();

    private List<User> adminUsers = new ArrayList<>();

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

    public AutoImport getAutoImport() {
        return autoImport;
    }

    public void setAutoImport(AutoImport autoImport) {
        this.autoImport = autoImport;
    }

    public List<PictureSize> getPictureSizes() {
        return pictureSizes;
    }

    public void setPictureSizes(List<PictureSize> pictureSizes) {
        this.pictureSizes = pictureSizes;
    }

    public List<User> getAdminUsers() {
        return adminUsers;
    }

    public void setAdminUsers(List<User> adminUsers) {
        this.adminUsers = adminUsers;
    }

    public class AutoImport {
        private boolean enabled;

        private String schedule;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getSchedule() {
            return schedule;
        }

        public void setSchedule(String schedule) {
            this.schedule = schedule;
        }
    }

    public static class QuestionMapping {
        private String id;

        private String question;

        private String description;

        private Answer defaultAnswer;

        private List<QuestionOrganisationGroupMapping> groups = new ArrayList<>();

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

        public List<QuestionOrganisationGroupMapping> getGroups() {
            return groups;
        }

        public void setGroups(List<QuestionOrganisationGroupMapping> groups) {
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

        public static class QuestionOrganisationGroupMapping {
            private OrganisationType organisationType;
            private String phrase;
            private Answer answer;

            public OrganisationType getOrganisationType() {
                return organisationType;
            }

            public void setOrganisationType(OrganisationType organisationType) {
                this.organisationType = organisationType;
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

    public static class User {
        private String username;
        private String password;
        private boolean printPassword;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public boolean isPrintPassword() {
            return printPassword;
        }

        public void setPrintPassword(boolean printPassword) {
            this.printPassword = printPassword;
        }

        @Override
        public String toString() {
            return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
        }
    }

}
