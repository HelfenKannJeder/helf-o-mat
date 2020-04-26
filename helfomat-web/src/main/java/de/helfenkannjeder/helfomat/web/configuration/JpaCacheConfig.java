package de.helfenkannjeder.helfomat.web.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.helfenkannjeder.helfomat.core.ProfileRegistry;
import de.helfenkannjeder.helfomat.infrastructure.elasticsearch.ElasticsearchConfiguration;
import de.helfenkannjeder.helfomat.infrastructure.elasticsearch.organization.ElasticsearchOrganizationRepository;
import de.helfenkannjeder.helfomat.infrastructure.jpa.EventRepository;
import de.helfenkannjeder.helfomat.infrastructure.jpa.JpaEventOrganizationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author Valentin Zickner
 */
@Configuration
@Profile("!" + ProfileRegistry.TEST)
public class JpaCacheConfig {

    @Bean
    public JpaEventOrganizationRepository jpaOrganizationRepository(
        ElasticsearchConfiguration elasticsearchConfiguration,
        ElasticsearchTemplate elasticsearchTemplate,
        @Value("classpath:/mapping/organization.json") Resource organizationMapping,
        ObjectMapper objectMapper,
        EventRepository eventRepository
    ) throws IOException {
        ElasticsearchOrganizationRepository elasticsearchOrganizationRepository = new ElasticsearchOrganizationRepository(
            elasticsearchConfiguration,
            elasticsearchTemplate,
            elasticsearchConfiguration.getIndex() + "-jpa-cache"
        );
        String mapping = StreamUtils.copyToString(organizationMapping.getInputStream(), StandardCharsets.UTF_8);
        elasticsearchOrganizationRepository.createIndex(mapping);
        return new JpaEventOrganizationRepository(objectMapper, elasticsearchOrganizationRepository, eventRepository);
    }

}
