package de.helfenkannjeder.helfomat.api.picture

import de.helfenkannjeder.helfomat.api.Roles
import de.helfenkannjeder.helfomat.core.picture.PictureId
import de.helfenkannjeder.helfomat.core.picture.PictureStorageService
import org.springframework.security.access.annotation.Secured
import org.springframework.stereotype.Service
import java.io.InputStream
import java.nio.file.Path

/**
 * @author Valentin Zickner
 */
@Service
open class PictureApplicationService(
    val pictureStorageService: PictureStorageService
) {

    open fun getPicture(pictureId: PictureId): Path = pictureStorageService.getPicture(pictureId)

    open fun getPicture(pictureId: PictureId, size: String): Path = pictureStorageService.getPicture(pictureId, size)

    @Secured(Roles.ADMIN)
    open fun savePicture(pictureId: PictureId, inputStream: InputStream) = pictureStorageService.savePicture(pictureId, inputStream)

}