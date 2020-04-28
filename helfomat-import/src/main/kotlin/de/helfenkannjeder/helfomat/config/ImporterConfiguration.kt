package de.helfenkannjeder.helfomat.config

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * @author Valentin Zickner
 */
@ConfigurationProperties("helfomat.importer")
class ImporterConfiguration (
    var webApiUrl: String? = null
)