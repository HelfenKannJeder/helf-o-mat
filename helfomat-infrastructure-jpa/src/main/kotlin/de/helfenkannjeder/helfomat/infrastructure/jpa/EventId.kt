package de.helfenkannjeder.helfomat.infrastructure.jpa

import org.springframework.data.annotation.AccessType
import java.io.Serializable
import java.util.*
import javax.persistence.Column
import javax.persistence.Embeddable

/**
 * @author Valentin Zickner
 */
@Embeddable
@AccessType(AccessType.Type.FIELD)
data class EventId (
    @field:Column(name = "eventId") val value: UUID = UUID.randomUUID()
) : Serializable