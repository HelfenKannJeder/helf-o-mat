package de.helfenkannjeder.helfomat.infrastructure.typo3.domain

import org.hibernate.annotations.Where
import javax.persistence.*

/**
 * @author Valentin Zickner
 */
@Entity(name = "tx_helfenkannjeder_domain_model_group")
data class TGroup (
    @Id
    var uid: Long,
    var name: String,
    var description: String?,
    var minimumAge: Int,
    var maximumAge: Int,
    var website: String?,

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tx_helfenkannjeder_group_contactperson_mm", joinColumns = [JoinColumn(name = "uid_local")], inverseJoinColumns = [JoinColumn(name = "uid_foreign")])
    @Where(clause = "deleted=0")
    var contactPersons: List<TEmployee>

)