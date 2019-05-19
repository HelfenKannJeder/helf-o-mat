package de.helfenkannjeder.helfomat.web.configuration;

import de.helfenkannjeder.helfomat.infrastructure.elasticsearch.ElasticsearchConfiguration;
import de.helfenkannjeder.helfomat.infrastructure.elasticsearch.organisation.ElasticsearchOrganisationRepository;
import de.helfenkannjeder.helfomat.infrastructure.kafka.KafkaOrganisationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;

@Configuration
public class KafkaCacheConfig {

    @Bean
    public KafkaOrganisationRepository kafkaOrganisationRepository(
        ElasticsearchConfiguration elasticsearchConfiguration,
        ElasticsearchTemplate elasticsearchTemplate,
        @Value("classpath:/mapping/organisation.json") Resource organisationMapping
    ) throws IOException {
        ElasticsearchOrganisationRepository elasticsearchOrganisationRepository = new ElasticsearchOrganisationRepository(
            elasticsearchConfiguration,
            elasticsearchTemplate,
            elasticsearchConfiguration.getIndex() + "-kafka-cache"
        );
        String mapping = StreamUtils.copyToString(organisationMapping.getInputStream(), Charset.forName("UTF8"));
        elasticsearchOrganisationRepository.createIndex(mapping);
        return new KafkaOrganisationRepository(
            elasticsearchOrganisationRepository
        );
    }

}
