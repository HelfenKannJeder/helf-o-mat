package de.helfenkannjeder.helfomat.core.question;

/**
 * @author Valentin Zickner
 */
public class Question {
    private QuestionId id;
    private String question;
    private String description;

    public Question() {
    }

    public QuestionId getId() {
        return id;
    }

    public void setId(QuestionId id) {
        this.id = id;
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

}
