package de.helfenkannjeder.helfomat.infrastructure.jpa

import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import de.helfenkannjeder.helfomat.core.organization.OrganizationId
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEvent
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.OffsetDateTime
import javax.persistence.*

/**
 * @author Valentin Zickner
 */
@Entity
@EntityListeners(AuditingEntityListener::class)
@TypeDefs(TypeDef(name = "jsonb", typeClass = JsonBinaryType::class))
data class Event(

    @EmbeddedId
    var eventId: EventId,

    @Embedded
    var organizationId: OrganizationId,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var domainEvent: OrganizationEvent,

    @CreatedDate
    @Column(columnDefinition = "TIMESTAMPTZ")
    var createdDate: OffsetDateTime? = null,

    @CreatedBy
    var createdBy: String? = null

)