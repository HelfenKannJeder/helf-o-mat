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
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Profile
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClientException
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
) : ApplicationContextAware, ApplicationListener<ContextRefreshedEvent> {

    private var applicationContext: ApplicationContext? = null

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.applicationContext = applicationContext
    }

    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        if (event.applicationContext == applicationContext) {
            this.run()
        }
    }

    private fun run() {
        try {
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
        } catch (e: RestClientException) {
            LOG.error("Failed to download organization updates", e)
        }
    }

    companion object {
        val LOG: Logger = LoggerFactory.getLogger(javaClass)
    }

}