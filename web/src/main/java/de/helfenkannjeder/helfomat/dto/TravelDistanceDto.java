package de.helfenkannjeder.helfomat.dto;

public class TravelDistanceDto {
    private TravelModeDto travelMode;
    private Long timeInSeconds;
    private Long distanceInMeters;

    public TravelModeDto getTravelMode() {
        return travelMode;
    }

    public void setTravelMode(TravelModeDto travelMode) {
        this.travelMode = travelMode;
    }

    public Long getTimeInSeconds() {
        return timeInSeconds;
    }

    public void setTimeInSeconds(Long timeInSeconds) {
        this.timeInSeconds = timeInSeconds;
    }

    public Long getDistanceInMeters() {
        return distanceInMeters;
    }

    public void setDistanceInMeters(Long distanceInMeters) {
        this.distanceInMeters = distanceInMeters;
    }
}
