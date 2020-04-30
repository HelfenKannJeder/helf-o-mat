package de.helfenkannjeder.helfomat.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

/**
 * @author Valentin Zickner
 */
@ConstructorBinding
@ConfigurationProperties("helfomat.importer")
class ImporterConfiguration (
    var webApiUrl: String
)