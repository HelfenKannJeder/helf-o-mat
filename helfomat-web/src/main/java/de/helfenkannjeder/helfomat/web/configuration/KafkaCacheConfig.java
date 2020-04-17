package de.helfenkannjeder.helfomat.web.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.helfenkannjeder.helfomat.core.ProfileRegistry;
import de.helfenkannjeder.helfomat.infrastructure.elasticsearch.ElasticsearchConfiguration;
import de.helfenkannjeder.helfomat.infrastructure.elasticsearch.organization.ElasticsearchOrganizationRepository;
import de.helfenkannjeder.helfomat.infrastructure.kafka.KafkaOrganizationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;

@Configuration
@Profile(ProfileRegistry.KAFKA)
@Import(KafkaAutoConfiguration.class)
public class KafkaCacheConfig {

    @Bean
    public KafkaOrganizationRepository kafkaOrganizationRepository(
        ElasticsearchConfiguration elasticsearchConfiguration,
        ElasticsearchTemplate elasticsearchTemplate,
        @Value("classpath:/mapping/organization.json") Resource organizationMapping,
        ObjectMapper objectMapper
    ) throws IOException {
        ElasticsearchOrganizationRepository elasticsearchOrganizationRepository = new ElasticsearchOrganizationRepository(
            elasticsearchConfiguration,
            elasticsearchTemplate,
            elasticsearchConfiguration.getIndex() + "-kafka-cache"
        );
        String mapping = StreamUtils.copyToString(organizationMapping.getInputStream(), Charset.forName("UTF8"));
        elasticsearchOrganizationRepository.createIndex(mapping);
        return new KafkaOrganizationRepository(objectMapper, elasticsearchOrganizationRepository);
    }

}
