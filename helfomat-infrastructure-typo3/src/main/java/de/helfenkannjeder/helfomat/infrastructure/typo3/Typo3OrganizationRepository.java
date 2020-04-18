package de.helfenkannjeder.helfomat.infrastructure.typo3;

import de.helfenkannjeder.helfomat.infrastructure.typo3.domain.TOrganization;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.stream.Stream;

/**
 * @author Valentin Zickner
 */
public interface Typo3OrganizationRepository extends PagingAndSortingRepository<TOrganization, String> {

    @Query("select organisation from tx_helfenkannjeder_domain_model_organisation organisation " +
        "where organisation.deleted = 0 " +
        "and organisation.hidden = 0 " +
        "and organisation.organizationtype <> null")
    Stream<TOrganization> findAvailable(Pageable pageable);

}