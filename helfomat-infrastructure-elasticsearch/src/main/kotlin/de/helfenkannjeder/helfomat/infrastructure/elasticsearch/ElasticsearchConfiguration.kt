package de.helfenkannjeder.helfomat.infrastructure.elasticsearch

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

/**
 * @author Valentin Zickner
 */
@Component
@ConfigurationProperties("elasticsearch")
class ElasticsearchConfiguration {
    var index: String? = null
    var type: TypeConfiguration = TypeConfiguration()

    class TypeConfiguration {
        var organization: String? = null
    }
}