package de.helfenkannjeder.helfomat.dto;

import java.util.List;

/**
 * @author Valentin Zickner
 */
public class SearchRequestDto {
    private List<QuestionAnswerDto> answers;
    private GeoPointDto position;
    private double distance;

    public List<QuestionAnswerDto> getAnswers() {
        return answers;
    }

    public void setAnswers(List<QuestionAnswerDto> answers) {
        this.answers = answers;
    }

    public GeoPointDto getPosition() {
        return position;
    }

    public void setPosition(GeoPointDto position) {
        this.position = position;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
