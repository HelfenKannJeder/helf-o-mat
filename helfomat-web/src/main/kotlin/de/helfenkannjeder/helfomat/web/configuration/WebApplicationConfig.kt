package de.helfenkannjeder.helfomat.web.configuration

import de.helfenkannjeder.helfomat.api.geopoint.GeoPointConverter
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
open class WebApplicationConfig(private val geoPointConverter: GeoPointConverter) : WebMvcConfigurer {

    override fun addFormatters(registry: FormatterRegistry) {
        registry.addConverter(geoPointConverter)
    }

}