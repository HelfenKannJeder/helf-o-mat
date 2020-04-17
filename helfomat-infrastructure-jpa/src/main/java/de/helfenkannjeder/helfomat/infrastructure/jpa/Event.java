package de.helfenkannjeder.helfomat.infrastructure.jpa;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import de.helfenkannjeder.helfomat.core.organization.OrganizationId;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEvent;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import java.time.OffsetDateTime;

/**
 * @author Valentin Zickner
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@TypeDefs({
    @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
public class Event {

    @EmbeddedId
    private EventId eventId;

    @Embedded
    private OrganizationId organizationId;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private OrganizationEvent domainEvent;

    @CreatedDate
    @Column(columnDefinition = "TIMESTAMPTZ")
    private OffsetDateTime createdDate;

    @CreatedBy
    private String createdBy;

    protected Event() {
    }

    public Event(EventId eventId, OrganizationEvent organizationEvent) {
        this.eventId = eventId;
        this.organizationId = organizationEvent.getOrganizationId();
        this.domainEvent = organizationEvent;
    }

    public EventId getEventId() {
        return eventId;
    }

    public OrganizationId getOrganizationId() {
        return organizationId;
    }

    public OrganizationEvent getDomainEvent() {
        return domainEvent;
    }

    public OffsetDateTime getCreatedDate() {
        return createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    @Override
    public String toString() {
        return "Event{" +
            "eventId=" + eventId +
            ", createdDate=" + createdDate +
            ", createdBy='" + createdBy + '\'' +
            ", domainEvent=" + domainEvent +
            '}';
    }
}
