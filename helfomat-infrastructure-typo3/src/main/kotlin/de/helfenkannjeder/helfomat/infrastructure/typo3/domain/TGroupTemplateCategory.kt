package de.helfenkannjeder.helfomat.infrastructure.typo3.domain

import org.hibernate.annotations.OrderBy
import org.hibernate.annotations.Where
import javax.persistence.*

/**
 * @author Valentin Zickner
 */
@Entity(name = "tx_helfenkannjeder_domain_model_grouptemplatecategory")
class TGroupTemplateCategory (
    @Id
    var uid: Long,
    var name: String?,
    var description: String?,

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "grouptemplatecategory")
    @Where(clause = "deleted=0")
    @OrderBy(clause = "sort")
    var groupTemplates: List<TGroupTemplate>?

)