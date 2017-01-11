package de.helfenkannjeder.helfomat.dto;

/**
 * @author Valentin Zickner
 */
public class QuestionAnswerDto {
    private String id;
    private int answer;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }
}
