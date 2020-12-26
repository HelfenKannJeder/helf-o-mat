package de.helfenkannjeder.helfomat.core.contact

import org.springframework.data.annotation.AccessType
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable

/**
 * @author Valentin Zickner
 */
@Embeddable
@AccessType(AccessType.Type.FIELD)
data class ContactRequestContactPerson(
    @Column(name = "contactPersonFirstname")
    var firstname: String,
    @Column(name = "contactPersonLastname")
    var lastname: String?,
    @Column(name = "contactPersonEmail")
    var email: String,
    @Column(name = "contactPersonTelephone")
    var telephone: String?
) : Serializable