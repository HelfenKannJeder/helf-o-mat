package de.helfenkannjeder.helfomat.infrastructure.elasticsearch

import org.elasticsearch.client.RestHighLevelClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentEntity
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentProperty
import org.springframework.data.mapping.context.MappingContext


@Configuration
open class ElasticsearchConfig {

    @Bean
    open fun elasticsearchRestTemplate(client: RestHighLevelClient, mappingContext: MappingContext<out ElasticsearchPersistentEntity<*>, ElasticsearchPersistentProperty>): ElasticsearchRestTemplate {
        return ElasticsearchRestTemplate(client, CustomMappingElasticsearchConverter(mappingContext))
    }

}