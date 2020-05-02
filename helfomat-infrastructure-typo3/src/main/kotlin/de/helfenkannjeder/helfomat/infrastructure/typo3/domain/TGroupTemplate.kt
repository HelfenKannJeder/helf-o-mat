package de.helfenkannjeder.helfomat.infrastructure.typo3.domain

import javax.persistence.Entity
import javax.persistence.Id

/**
 * @author Valentin Zickner
 */
@Entity(name = "tx_helfenkannjeder_domain_model_grouptemplate")
data class TGroupTemplate (
    @Id
    var uid: Long,
    var name: String?,
    var description: String?,
    var suggestion: String?

)