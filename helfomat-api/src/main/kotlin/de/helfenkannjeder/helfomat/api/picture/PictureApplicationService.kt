package de.helfenkannjeder.helfomat.api.picture

import de.helfenkannjeder.helfomat.api.Roles
import de.helfenkannjeder.helfomat.core.picture.Picture
import de.helfenkannjeder.helfomat.core.picture.PictureId
import de.helfenkannjeder.helfomat.core.picture.PictureRepository
import de.helfenkannjeder.helfomat.core.picture.PictureStorageService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.access.annotation.Secured
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import java.io.InputStream
import java.nio.file.Path

/**
 * @author Valentin Zickner
 */
@Service
open class PictureApplicationService(
    val pictureRepository: PictureRepository,
    val pictureStorageService: PictureStorageService
) {

    @PreAuthorize("@pictureAuthorizationService.canAccess(#pictureId)")
    open fun getPicture(pictureId: PictureId): Path = pictureStorageService.getPicture(pictureId)

    @PreAuthorize("@pictureAuthorizationService.canAccess(#pictureId)")
    open fun getPicture(pictureId: PictureId, size: String): Path = pictureStorageService.getPicture(pictureId, size)

    @Secured(Roles.ADMIN, Roles.REVIEWER, Roles.USER)
    open fun savePicture(pictureId: PictureId, inputStream: InputStream, contentType: String?): PictureId {
        if (pictureRepository.findByIdOrNull(pictureId) != null) {
            throw IllegalStateException("PictureId already exists ${pictureId}")
        }
        pictureStorageService.savePicture(pictureId, inputStream)
        pictureRepository.save(Picture(pictureId, false, pictureStorageService.getContentType(pictureId)))
        return pictureId
    }

}