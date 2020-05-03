package de.helfenkannjeder.helfomat.core.approval

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
data class ApprovalId(
    @Column(name = "approvalId") val value: UUID = UUID.randomUUID()
) : Serializable {

    @Suppress("unused")
    constructor(value: String) : this(UUID.fromString(value))

}