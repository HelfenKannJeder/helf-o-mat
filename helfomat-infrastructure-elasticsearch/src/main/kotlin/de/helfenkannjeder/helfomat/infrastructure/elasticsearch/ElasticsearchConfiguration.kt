package de.helfenkannjeder.helfomat.infrastructure.elasticsearch

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

/**
 * @author Valentin Zickner
 */
@ConstructorBinding
@ConfigurationProperties("elasticsearch")
data class ElasticsearchConfiguration(
    var index: String,
    var type: TypeConfiguration
) {

    data class TypeConfiguration(
        var organization: String
    )
}