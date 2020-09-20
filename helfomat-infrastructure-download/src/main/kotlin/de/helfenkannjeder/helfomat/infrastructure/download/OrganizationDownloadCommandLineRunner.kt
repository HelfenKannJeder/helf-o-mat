package de.helfenkannjeder.helfomat.infrastructure.download

import de.helfenkannjeder.helfomat.api.picture.PictureConfiguration
import de.helfenkannjeder.helfomat.api.picture.PictureStorageService
import de.helfenkannjeder.helfomat.api.picture.ResizeImageService
import de.helfenkannjeder.helfomat.core.ProfileRegistry
import de.helfenkannjeder.helfomat.core.organization.OrganizationRepository
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEvent.Companion.toPictureIds
import de.helfenkannjeder.helfomat.core.picture.Picture
import de.helfenkannjeder.helfomat.core.picture.PictureRepository
import org.apache.tika.Tika
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.io.File
import java.nio.file.Files

@Component
@Profile(ProfileRegistry.ENABLE_DOWNLOAD)
@EnableConfigurationProperties(PictureConfiguration::class)
open class OrganizationDownloadCommandLineRunner(
    private val organizationDownloadService: OrganizationDownloadService,
    private val organizationRepository: OrganizationRepository,
    private val pictureRepository: PictureRepository,
    private val pictureStorageService: PictureStorageService,
    private val pictureConfiguration: PictureConfiguration,
    private val resizeImageService: ResizeImageService,
    private val applicationEventPublisher: ApplicationEventPublisher
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        val events = organizationDownloadService.getOrganizationList()
            .flatMap { it.compareTo(organizationRepository.findOne(it.id.value)) }
        toPictureIds(events)
            .filter { !pictureRepository.existsById(it) }
            .forEach {
                val bytes = organizationDownloadService.getPicture(it) ?: return@forEach
                val tempFile = File.createTempFile("cache", null)
                Files.write(tempFile.toPath(), bytes)
                pictureStorageService.savePicture(it, tempFile.inputStream(), tempFile.length())
                val contentType = Tika().detect(tempFile)
                for (pictureSize in pictureConfiguration.pictureSizes) {
                    val inputStreamToResize = tempFile.inputStream()
                    val resizedImage = resizeImageService.resize(inputStreamToResize, pictureSize.width, pictureSize.height, contentType)
                    pictureStorageService.savePicture(it, resizedImage.first, resizedImage.second, pictureSize.name)
                }
                tempFile.delete()
                pictureRepository.save(Picture(it, true, contentType))
            }
        events.forEach { applicationEventPublisher.publishEvent(it) }
    }

}