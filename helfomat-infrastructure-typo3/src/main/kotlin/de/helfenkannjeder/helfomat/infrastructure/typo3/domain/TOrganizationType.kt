package de.helfenkannjeder.helfomat.infrastructure.typo3.domain

import org.hibernate.annotations.OrderBy
import org.hibernate.annotations.Where
import javax.persistence.*

/**
 * @author Valentin Zickner
 */
@Entity(name = "tx_helfenkannjeder_domain_model_organisationtype")
data class TOrganizationType (
    @Id
    var uid: String? = null,
    var name: String,
    var acronym: String? = null,
    var picture: String? = null,

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "organisationtype")
    @Where(clause = "deleted=0")
    @OrderBy(clause = "sort")
    var groupTemplateCategories: List<TGroupTemplateCategory>? = emptyList(),
    var deleted: Boolean = false,
    var hidden: Boolean = false,
    var registerable: Boolean = true

)