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
class EventId (
    @field:Column(name = "eventId") val value: UUID = UUID.randomUUID()
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as EventId
        if (value != other.value) return false
        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

    override fun toString(): String {
        return "EventId(value=$value)"
    }


}