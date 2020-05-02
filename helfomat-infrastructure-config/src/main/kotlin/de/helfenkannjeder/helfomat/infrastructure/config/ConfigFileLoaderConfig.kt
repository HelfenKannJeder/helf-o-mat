package de.helfenkannjeder.helfomat.infrastructure.config

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer
import org.springframework.core.io.ClassPathResource

/**
 * @author Valentin Zickner
 */
@Configuration
open class ConfigFileLoaderConfig {

    @Bean
    open fun properties(): PropertySourcesPlaceholderConfigurer {
        val propertySourcesPlaceholderConfigurer = PropertySourcesPlaceholderConfigurer()
        val yaml = YamlPropertiesFactoryBean()
        yaml.setResources(ClassPathResource("config-repository-defaults.yml"))
        val properties = yaml.getObject()
        if (properties != null) {
            propertySourcesPlaceholderConfigurer.setProperties(properties)
        }
        return propertySourcesPlaceholderConfigurer
    }

}