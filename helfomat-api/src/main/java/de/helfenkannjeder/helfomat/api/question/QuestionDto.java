package de.helfenkannjeder.helfomat.api.question;

/**
 * @author Valentin Zickner
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal", "unused"})
public class QuestionDto {
    private String id;
    private String question;
    private String description;

    private QuestionDto() {
    }

    public QuestionDto(String id, String question, String description) {
        this.id = id;
        this.question = question;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
