package de.helfenkannjeder.helfomat.infrastructure.jpa;

import org.springframework.data.annotation.AccessType;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Valentin Zickner
 */
@Embeddable
@AccessType(AccessType.Type.FIELD)
public class EventId implements Serializable {

    @Column(name = "event_id")
    private String value;

    public EventId() {
        this(UUID.randomUUID().toString());
    }

    public EventId(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventId eventId = (EventId) o;
        return Objects.equals(value, eventId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "EventId{" +
            "value='" + value + '\'' +
            '}';
    }

}
