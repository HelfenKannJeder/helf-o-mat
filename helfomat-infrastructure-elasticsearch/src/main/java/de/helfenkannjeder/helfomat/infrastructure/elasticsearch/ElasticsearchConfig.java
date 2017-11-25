package de.helfenkannjeder.helfomat.infrastructure.elasticsearch;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationRepository;
import de.helfenkannjeder.helfomat.infrastructure.elasticsearch.organisation.ElasticsearchOrganisationRepository;
import org.elasticsearch.client.Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.EntityMapper;

import java.io.IOException;

@Configuration
public class ElasticsearchConfig {

    @Bean
    public ElasticsearchTemplate elasticsearchTemplate(Client client) {
        return new ElasticsearchTemplate(client, new CustomEntityMapper());
    }

    @Bean
    public OrganisationRepository organisationRepository(ElasticsearchConfiguration elasticsearchConfiguration, Client client) {
        return new ElasticsearchOrganisationRepository(
            elasticsearchConfiguration,
            elasticsearchTemplate(client),
            elasticsearchConfiguration.getIndex()
        );
    }

    public static class CustomEntityMapper implements EntityMapper {

        private final ObjectMapper objectMapper;

        CustomEntityMapper() {
            objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.registerModule(new Jdk8Module());
        }

        @Override
        public String mapToString(Object object) throws IOException {
            return objectMapper.writeValueAsString(object);
        }

        @Override
        public <T> T mapToObject(String source, Class<T> clazz) throws IOException {
            return objectMapper.readValue(source, clazz);
        }

    }

}