package de.helfenkannjeder.helfomat.web.configuration

import de.helfenkannjeder.helfomat.core.ProfileRegistry
import de.helfenkannjeder.helfomat.infrastructure.elasticsearch.ElasticsearchConfiguration
import de.helfenkannjeder.helfomat.infrastructure.elasticsearch.organization.ElasticsearchOrganizationRepository
import de.helfenkannjeder.helfomat.infrastructure.jpa.EventRepository
import de.helfenkannjeder.helfomat.infrastructure.jpa.JpaEventOrganizationRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.io.Resource
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate
import org.springframework.util.StreamUtils
import java.nio.charset.StandardCharsets

/**
 * @author Valentin Zickner
 */
@Configuration
@EnableConfigurationProperties(ElasticsearchConfiguration::class)
@Profile("!" + ProfileRegistry.TEST)
open class JpaCacheConfig {

    @Bean
    open fun jpaOrganizationRepository(
        elasticsearchConfiguration: ElasticsearchConfiguration,
        elasticsearchTemplate: ElasticsearchTemplate,
        @Value("classpath:/mapping/organization.json") organizationMapping: Resource,
        eventRepository: EventRepository
    ): JpaEventOrganizationRepository {
        val elasticsearchOrganizationRepository = ElasticsearchOrganizationRepository(
            elasticsearchConfiguration,
            elasticsearchTemplate,
            elasticsearchConfiguration.index + "-jpa-cache"
        )
        val mapping = StreamUtils.copyToString(organizationMapping.inputStream, StandardCharsets.UTF_8)
        elasticsearchOrganizationRepository.createIndex(mapping)
        return JpaEventOrganizationRepository(elasticsearchOrganizationRepository, eventRepository)
    }

}