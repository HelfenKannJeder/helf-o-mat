package de.helfenkannjeder.helfomat.core.organization

import java.io.Serializable
import java.util.*
import javax.persistence.Column
import javax.persistence.Embeddable

/**
 * @author Valentin Zickner
 */
@Embeddable
data class OrganizationId(
    @field:Column(name = "organizationId") val value: String = UUID.randomUUID().toString()
) : Serializable