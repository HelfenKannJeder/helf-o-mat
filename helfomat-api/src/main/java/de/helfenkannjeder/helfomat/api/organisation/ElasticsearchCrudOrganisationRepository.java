package de.helfenkannjeder.helfomat.api.organisation;

import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

/**
 * @author Valentin Zickner
 */
public interface ElasticsearchCrudOrganisationRepository extends ElasticsearchCrudRepository<Organisation, String> {
}
