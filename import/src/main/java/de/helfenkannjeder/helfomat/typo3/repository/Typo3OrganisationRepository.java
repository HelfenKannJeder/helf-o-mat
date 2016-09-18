package de.helfenkannjeder.helfomat.typo3.repository;

import de.helfenkannjeder.helfomat.typo3.domain.TOrganisation;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Valentin Zickner
 */
public interface Typo3OrganisationRepository extends PagingAndSortingRepository<TOrganisation, String> {

}
