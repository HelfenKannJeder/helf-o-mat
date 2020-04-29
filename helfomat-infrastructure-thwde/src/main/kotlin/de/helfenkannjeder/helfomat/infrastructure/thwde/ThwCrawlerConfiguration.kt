package de.helfenkannjeder.helfomat.infrastructure.thwde

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

/**
 * @author Valentin Zickner
 */
@Component
@ConfigurationProperties(prefix = "crawler.thw")
class ThwCrawlerConfiguration {
    var domain: String? = null
    var isFollowDomainNames = true
    var resultsPerPage = 0
    var httpRequestTimeout = 0
    var mapPin: String? = null
}