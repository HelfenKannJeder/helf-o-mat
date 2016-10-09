package de.helfenkannjeder.helfomat.domain;

import java.util.List;

/**
 * @author Valentin Zickner
 */
public class SearchRequest {
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
