package de.helfenkannjeder.helfomat.dto;

import de.helfenkannjeder.helfomat.domain.Answer;
import de.helfenkannjeder.helfomat.domain.GeoPoint;

import java.util.List;

/**
 * @author Valentin Zickner
 */
public class SearchRequestDto {
    private List<Answer> answers;
    private GeoPoint position;
    private double distance;

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public GeoPoint getPosition() {
        return position;
    }

    public void setPosition(GeoPoint position) {
        this.position = position;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
