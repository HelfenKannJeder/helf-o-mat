package de.helfenkannjeder.helfomat.infrastructure.typo3.domain

import javax.persistence.Entity
import javax.persistence.Id

/**
 * @author Valentin Zickner
 */
@Entity(name = "tx_helfenkannjeder_domain_model_employee")
data class TEmployee(
    @Id
    var uid: Long,
    var rank: String?,
    var surname: String?,
    var prename: String?,
    var motivation: String?,
    var birthday: Long,
    var pictures: String?,
    var mail: String?,
    var street: String?,
    var city: String?,
    var mobilephone: String?,
    var headline: String?,
    var iscontact: Int,
    var telephone: String?
) {
    fun isIscontact() = iscontact == 1
}