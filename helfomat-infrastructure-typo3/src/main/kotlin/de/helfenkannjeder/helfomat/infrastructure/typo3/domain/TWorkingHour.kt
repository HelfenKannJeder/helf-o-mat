package de.helfenkannjeder.helfomat.infrastructure.typo3.domain

import org.hibernate.annotations.Where
import javax.persistence.*

/**
 * @author Valentin Zickner
 */
@Entity(name = "tx_helfenkannjeder_domain_model_workinghour")
data class TWorkingHour (
    @Id
    var uid: Long,
    var day: Int,
    var starttimehour: Int,
    var stoptimehour: Int,
    var starttimeminute: Int,
    var stoptimeminute: Int,
    var addition: String?,

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tx_helfenkannjeder_workinghour_group_mm", joinColumns = [JoinColumn(name = "uid_local")], inverseJoinColumns = [JoinColumn(name = "uid_foreign")])
    @Where(clause = "deleted=0")
    var groups: List<TGroup>

)