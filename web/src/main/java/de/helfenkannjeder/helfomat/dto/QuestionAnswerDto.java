package de.helfenkannjeder.helfomat.dto;

/**
 * @author Valentin Zickner
 */
public class QuestionAnswerDto {
    private String id;
    private AnswerDto answer;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AnswerDto getAnswer() {
        return answer;
    }

    public void setAnswer(AnswerDto answer) {
        this.answer = answer;
    }
}
