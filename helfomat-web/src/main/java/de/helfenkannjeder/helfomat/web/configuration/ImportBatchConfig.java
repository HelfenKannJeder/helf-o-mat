package de.helfenkannjeder.helfomat.web.configuration;

import de.helfenkannjeder.helfomat.core.IndexManager;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationRepository;
import de.helfenkannjeder.helfomat.infrastructure.elasticsearch.ElasticsearchConfiguration;
import de.helfenkannjeder.helfomat.infrastructure.elasticsearch.organisation.ElasticsearchOrganisationRepository;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

/**
 * @author Valentin Zickner
 */
@Configuration
public class ImportBatchConfig {

    @Bean
    @JobScope
    public OrganisationRepository importOrganisationRepository(ElasticsearchConfiguration elasticsearchConfiguration, ElasticsearchTemplate
        elasticsearchTemplate, IndexManager indexManager) {
        return new ElasticsearchOrganisationRepository(
            elasticsearchConfiguration,
            elasticsearchTemplate,
            indexManager.getCurrentIndex()
        );
    }

}
