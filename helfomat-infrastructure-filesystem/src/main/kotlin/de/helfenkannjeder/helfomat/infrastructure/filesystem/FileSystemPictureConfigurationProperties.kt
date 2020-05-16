package de.helfenkannjeder.helfomat.infrastructure.filesystem

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("helfomat.picture")
data class FileSystemPictureConfigurationProperties(
    var pictureFolder: String
)