package de.helfenkannjeder.helfomat.infrastructure.google.maps

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

/**
 * @author Valentin Zickner
 */
@ConstructorBinding
@ConfigurationProperties("googlemaps")
data class GoogleMapsConfiguration(
    var apiKey: String
)