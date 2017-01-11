package de.helfenkannjeder.helfomat.dto;

import de.helfenkannjeder.helfomat.domain.BoundingBox;
import de.helfenkannjeder.helfomat.domain.GeoPoint;

/**
 * @author Valentin Zickner
 */
public class BoundingBoxRequestDto {
    private GeoPoint position;
    private double distance;
    private BoundingBox boundingBox;
    private int zoom;

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

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }

    public int getZoom() {
        return zoom;
    }

    public void setZoom(int zoom) {
        this.zoom = zoom;
    }
}
