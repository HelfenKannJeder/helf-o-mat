package de.helfenkannjeder.helfomat.infrastructure.typo3.domain

import org.hibernate.annotations.NotFound
import org.hibernate.annotations.NotFoundAction
import org.hibernate.annotations.OrderBy
import org.hibernate.annotations.Where
import javax.persistence.*

/**
 * @author Valentin Zickner
 */
@Entity(name = "tx_helfenkannjeder_domain_model_organisation")
data class TOrganization (
    @Id
    var uid: String? = null,
    var name: String,
    var description: String? = null,
    var website: String? = null,
    var logo: String? = null,
    var pictures: String? = null,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "organisationtype")
    @NotFound(action = NotFoundAction.IGNORE)
    var organizationtype: TOrganizationType,

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "organisation")
    @Where(clause = "deleted=0")
    var addresses: List<TAddress> = emptyList(),

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "defaultaddress")
    @Where(clause = "deleted=0")
    @NotFound(action = NotFoundAction.IGNORE)
    var defaultaddress: TAddress? = null,

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "organisation")
    @Where(clause = "deleted=0")
    @OrderBy(clause = "prename")
    var employees: List<TEmployee> = emptyList(),

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tx_helfenkannjeder_organisaton_contactperson_mm", joinColumns = [JoinColumn(name = "uid_local")], inverseJoinColumns = [JoinColumn(name = "uid_foreign")])
    @Where(clause = "deleted=0")
    var contactPersons: List<TEmployee> = emptyList(),

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "organisation")
    @Where(clause = "deleted=0")
    @OrderBy(clause = "sort")
    var groups: List<TGroup> = emptyList(),

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "organisation")
    @Where(clause = "deleted=0")
    @OrderBy(clause = "day, starttimehour, starttimeminute")
    var workinghours: List<TWorkingHour> = emptyList(),

    var deleted: Boolean = false,
    var hidden: Boolean = false

)