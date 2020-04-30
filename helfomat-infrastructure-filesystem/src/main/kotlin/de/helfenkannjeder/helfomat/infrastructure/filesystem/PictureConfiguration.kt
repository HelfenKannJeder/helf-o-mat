package de.helfenkannjeder.helfomat.infrastructure.filesystem

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.util.*

/**
 * @author Valentin Zickner
 */
@ConstructorBinding
@ConfigurationProperties("helfomat.picture")
data class PictureConfiguration(
    var pictureFolder: String,
    var pictureSizes: List<PictureSize> = ArrayList()
) {

    data class PictureSize (
        var name: String,
        var width: Int,
        var height: Int
    )

}