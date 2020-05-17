package de.helfenkannjeder.helfomat.infrastructure.azure

import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import de.helfenkannjeder.helfomat.core.ProfileRegistry
import de.helfenkannjeder.helfomat.core.picture.PictureId
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.Resource
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles(profiles = [ProfileRegistry.TEST, ProfileRegistry.AZURE])
class AzureBlobStoragePictureStorageServiceTest {

    @Autowired
    lateinit var pictureStorageService: AzureBlobStoragePictureStorageService

    @Value("classpath:/de/helfenkannjeder/helfomat/infrastructure/azure/test.jpg")
    lateinit var testImage: Resource

    @Test
    fun uploadAndDownloadFileToAzure() {
        val pictureId = PictureId()
        val existPicture: Boolean = pictureStorageService.existPicture(pictureId)
        assertThat(existPicture).isFalse()
        pictureStorageService.savePicture(pictureId, testImage.inputStream, testImage.contentLength())
        val existPictureAfterCreate: Boolean = pictureStorageService.existPicture(pictureId)
        assertThat(existPictureAfterCreate).isTrue()

        val picture = pictureStorageService.getPicture(pictureId)
        val pictureFromAzure = picture.readAllBytes()
        val pictureFromResources = testImage.inputStream.readAllBytes()
        assertThat(pictureFromAzure.contentEquals(pictureFromResources)).isTrue()

        pictureStorageService.deletePicture(pictureId)
    }

    @Test
    fun uploadAndDownloadFileToAzureWithTag() {
        val pictureId = PictureId()
        pictureStorageService.savePicture(pictureId, testImage.inputStream, testImage.contentLength(), "medium")
        val picture = pictureStorageService.getPicture(pictureId, "medium")
        val pictureFromAzure = picture.readAllBytes()
        val pictureFromResources = testImage.inputStream.readAllBytes()
        assertThat(pictureFromAzure.contentEquals(pictureFromResources)).isTrue()

        pictureStorageService.deletePicture(pictureId, "medium")
    }

    @Test
    fun uploadAndDownloadFileToAzureWithTagAndWithoutTag() {
        val pictureId = PictureId()
        pictureStorageService.savePicture(pictureId, testImage.inputStream, testImage.contentLength())
        pictureStorageService.savePicture(pictureId, testImage.inputStream, testImage.contentLength(), "medium")
        pictureStorageService.deletePicture(pictureId)
        pictureStorageService.deletePicture(pictureId, "medium")
    }

}