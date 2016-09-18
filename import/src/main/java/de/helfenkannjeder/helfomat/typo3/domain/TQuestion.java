package de.helfenkannjeder.helfomat.typo3.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author Valentin Zickner
 */
@Entity(name = "tx_helfenkannjeder_domain_model_helfomatquestion")
public class TQuestion {

    @Id
    private long uid;

    private String question;
    private String description;

    private long helfomat;
    private int sort;

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

    public long getHelfomat() {
        return helfomat;
    }

    public void setHelfomat(long helfomat) {
        this.helfomat = helfomat;
    }

    @Override
    public String toString() {
        return "TQuestion{" +
                "question='" + question + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TQuestion tQuestion = (TQuestion) o;

        return uid == tQuestion.uid;

    }

    @Override
    public int hashCode() {
        return (int) (uid ^ (uid >>> 32));
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
