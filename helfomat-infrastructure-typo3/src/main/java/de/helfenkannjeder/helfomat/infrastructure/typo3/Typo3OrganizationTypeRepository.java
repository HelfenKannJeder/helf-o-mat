package de.helfenkannjeder.helfomat.infrastructure.typo3;

import de.helfenkannjeder.helfomat.infrastructure.typo3.domain.TOrganizationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author Valentin Zickner
 */
public interface Typo3OrganizationTypeRepository extends JpaRepository<TOrganizationType, String> {

    @Query("select organisationType from tx_helfenkannjeder_domain_model_organisationtype organisationType " +
        "where organisationType.deleted = 0 " +
        "and organisationType.hidden = 0 " +
        "and organisationType.name = :organizationTypeName")
    TOrganizationType findByName(@Param("organizationTypeName") String organizationTypeName);

}
