package de.helfenkannjeder.helfomat.infrastructure.typo3

import de.helfenkannjeder.helfomat.infrastructure.typo3.domain.TOrganization
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository

/**
 * @author Valentin Zickner
 */
interface Typo3OrganizationRepository : PagingAndSortingRepository<TOrganization, String> {

    @Query("select organisation from tx_helfenkannjeder_domain_model_organisation organisation " +
        "where organisation.deleted = 0 " +
        "and organisation.hidden = 0 " +
        "and organisation.organizationtype <> null")
    fun findAvailable(pageable: Pageable): List<TOrganization>

}