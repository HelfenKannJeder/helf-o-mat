package de.helfenkannjeder.helfomat.domain;

import java.util.Objects;
import java.util.UUID;

/**
 * @author Valentin Zickner
 */
public class PictureId {

    private String value;

    public PictureId() {
        this.value = UUID.randomUUID().toString();
    }

    public PictureId(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PictureId pictureId = (PictureId) o;
        return Objects.equals(value, pictureId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "PictureId{" +
            "value='" + value + '\'' +
            '}';
    }
}
