package de.helfenkannjeder.helfomat.repository;

import de.helfenkannjeder.helfomat.domain.Organisation;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

/**
 * @author Valentin Zickner
 */
public interface OrganisationRepository extends ElasticsearchCrudRepository<Organisation, String> {
}
