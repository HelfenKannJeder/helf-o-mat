package de.helfenkannjeder.helfomat.api.picture

import de.helfenkannjeder.helfomat.api.Roles
import de.helfenkannjeder.helfomat.core.picture.Picture
import de.helfenkannjeder.helfomat.core.picture.PictureId
import de.helfenkannjeder.helfomat.core.picture.PictureRepository
import org.apache.tika.Tika
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.security.access.annotation.Secured
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import java.io.InputStream

/**
 * @author Valentin Zickner
 */
@Service
@EnableConfigurationProperties(PictureConfiguration::class)
open class PictureApplicationService(
    val pictureRepository: PictureRepository,
    val pictureStorageService: PictureStorageService,
    val pictureConfiguration: PictureConfiguration,
    val resizeImageService: ResizeImageService
) {

    @PreAuthorize("@pictureAuthorizationService.canAccess(#pictureId)")
    open fun getPicture(pictureId: PictureId): PictureDto {
        if (!pictureStorageService.existPicture(pictureId)) {
            throw PictureNotFoundException(pictureId)
        }
        ensurePictureExists(pictureId)
        val pictureInformation = pictureRepository.getOne(pictureId)
        val inputStream = pictureStorageService.getPicture(pictureId)
        return PictureDto(inputStream, pictureInformation.contentType)
    }

    @PreAuthorize("@pictureAuthorizationService.canAccess(#pictureId)")
    open fun getPicture(pictureId: PictureId, size: String): PictureDto {
        if (!pictureStorageService.existPicture(pictureId)) {
            throw PictureNotFoundException(pictureId)
        }
        ensurePictureExists(pictureId)
        val pictureInformation = pictureRepository.getOne(pictureId)
        val inputStream = pictureStorageService.getPicture(pictureId, size)
        return PictureDto(inputStream, pictureInformation.contentType)
    }

    @Secured(Roles.ADMIN, Roles.REVIEWER, Roles.USER)
    open fun savePicture(pictureId: PictureId, inputStream: InputStream, size: Long): PictureId {
        if (pictureRepository.existsById(pictureId)) {
            throw IllegalStateException("PictureId already exists ${pictureId}")
        }
        pictureStorageService.savePicture(pictureId, inputStream, size)
        val picture = pictureStorageService.getPicture(pictureId)
        val contentType = Tika().detect(picture)
        for (pictureSize in pictureConfiguration.pictureSizes) {
            val inputStreamToResize = pictureStorageService.getPicture(pictureId)
            val resizedImage = resizeImageService.resize(inputStreamToResize, pictureSize.width, pictureSize.height, contentType)
            pictureStorageService.savePicture(pictureId, resizedImage.first, resizedImage.second, pictureSize.name)
        }
        pictureRepository.save(Picture(pictureId, false, contentType))
        return pictureId
    }

    private fun ensurePictureExists(pictureId: PictureId) {
        if (!pictureRepository.existsById(pictureId)) {
            throw PictureNotFoundException(pictureId)
        }
    }

}