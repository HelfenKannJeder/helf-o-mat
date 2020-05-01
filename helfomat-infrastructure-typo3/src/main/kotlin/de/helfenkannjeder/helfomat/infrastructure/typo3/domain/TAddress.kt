package de.helfenkannjeder.helfomat.infrastructure.typo3.domain

import javax.persistence.Entity
import javax.persistence.Id

/**
 * @author Valentin Zickner
 */
@Entity(name = "tx_helfenkannjeder_domain_model_address")
data class TAddress (
    @Id
    var uid: Long,
    var street: String?,
    var addressappendix: String?,
    var city: String?,
    var zipcode: String?,
    var longitude: Double,
    var latitude: Double,
    var telephone: String?,
    var website: String?

)