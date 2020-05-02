package de.helfenkannjeder.helfomat.infrastructure.thwde

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

/**
 * @author Valentin Zickner
 */
@ConstructorBinding
@ConfigurationProperties("crawler.thw")
class ThwCrawlerConfiguration(
    var domain: String,
    var isFollowDomainNames: Boolean = true,
    var resultsPerPage: Int = 0,
    var httpRequestTimeout: Int = 0,
    var mapPin: String? = null
)