package de.helfenkannjeder.helfomat.dto;

/**
 * @author Valentin Zickner
 */
public class BoundingBoxRequestDto {
    private GeoPointDto position;
    private double distance;
    private BoundingBoxDto boundingBox;
    private int zoom;

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

    public BoundingBoxDto getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(BoundingBoxDto boundingBoxDto) {
        this.boundingBox = boundingBoxDto;
    }

    public int getZoom() {
        return zoom;
    }

    public void setZoom(int zoom) {
        this.zoom = zoom;
    }
}
