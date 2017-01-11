package de.helfenkannjeder.helfomat.dto;

/**
 * @author Valentin Zickner
 */
public class QuestionDto {
    private String id;
    private String question;
    private String description;
    private int position;

    public QuestionDto() {
    }

    QuestionDto(String question, String description, int position) {
        this.question = question;
        this.description = description;
        this.position = position;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public QuestionDto(String question) {
        this.question = question;
    }

    public QuestionDto(String id, String question, String description, int position) {
        this.id = id;
        this.question = question;
        this.description = description;
        this.position = position;
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

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
