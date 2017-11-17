package de.helfenkannjeder.helfomat.core.question;

/**
 * @author Valentin Zickner
 */
public class Question {
    private QuestionId id;
    private String question;
    private String description;
    private Answer answer = Answer.MAYBE;
    private int position;

    public Question() {
    }

    public Question(QuestionId id, String question, int position, Answer answer) {
        this.id = id;
        this.question = question;
        this.position = position;
        this.answer = answer;
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

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

}
