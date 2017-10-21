package de.helfenkannjeder.helfomat.infrastructure.typo3;

import de.helfenkannjeder.helfomat.infrastructure.typo3.domain.TOrganisation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author Valentin Zickner
 */
public interface Typo3OrganisationRepository extends PagingAndSortingRepository<TOrganisation, String> {

    @Query("select organisation from tx_helfenkannjeder_domain_model_organisation organisation " +
        "where organisation.deleted = 0 " +
        "and organisation.hidden = 0 " +
        "and organisation.organisationtype <> null")
    List<TOrganisation> findAvailable(Pageable pageable);

}
