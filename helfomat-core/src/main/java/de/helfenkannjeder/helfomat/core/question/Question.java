package de.helfenkannjeder.helfomat.core.question;

/**
 * @author Valentin Zickner
 */
public class Question {
    private long uid;
    private String question;
    private String description;
    private Answer answer = Answer.MAYBE;
    private int position;

    public Question() {
    }

    public Question(long uid, String question, int position, Answer answer) {
        this.uid = uid;
        this.question = question;
        this.position = position;
        this.answer = answer;
    }

    public Question(long uid, String question, String description, int position) {
        this.uid = uid;
        this.question = question;
        this.description = description;
        this.position = position;
    }

    public Question(Question question, Answer answer) {
        this.setUid(question.getUid());
        this.setQuestion(question.getQuestion());
        this.setAnswer(answer);
        this.setPosition(question.getPosition());
        this.setDescription(question.getDescription());
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
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
