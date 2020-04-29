package de.helfenkannjeder.helfomat.infrastructure.google.maps

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

/**
 * @author Valentin Zickner
 */
@Component
@ConfigurationProperties("googlemaps")
data class GoogleMapsConfiguration (
    var apiKey: String? = null
)