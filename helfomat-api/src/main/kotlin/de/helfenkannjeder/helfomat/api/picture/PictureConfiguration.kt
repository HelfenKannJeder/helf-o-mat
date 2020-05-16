package de.helfenkannjeder.helfomat.api.picture

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.util.*

/**
 * @author Valentin Zickner
 */
@ConstructorBinding
@ConfigurationProperties("helfomat.picture")
data class PictureConfiguration(
    var pictureSizes: List<PictureSize> = ArrayList()
) {

    data class PictureSize (
        var name: String,
        var width: Int?,
        var height: Int?
    )

}