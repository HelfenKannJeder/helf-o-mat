package de.helfenkannjeder.helfomat.infrastructure.filesystem

import de.helfenkannjeder.helfomat.api.picture.ResizeImageService
import de.helfenkannjeder.helfomat.core.picture.DownloadFailedException
import de.helfenkannjeder.helfomat.core.picture.DownloadService
import de.helfenkannjeder.helfomat.core.picture.PictureId
import de.helfenkannjeder.helfomat.core.picture.PictureStorageService
import de.helfenkannjeder.helfomat.infrastructure.filesystem.PictureConfiguration.PictureSize
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.assertj.core.api.ThrowableAssert.ThrowingCallable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.web.client.HttpClientErrorException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author Valentin Zickner
 */
@ExtendWith(MockitoExtension::class)
internal class FileSystemPictureStorageServiceTest {

    @Mock
    private lateinit var downloadService: DownloadService

    @Mock
    private lateinit var resizeImageService: ResizeImageService

    private var pictureConfiguration: PictureConfiguration = PictureConfiguration(
        CONF_FOLDER,
        listOf(PictureSize(
            name = "test-size",
            width = 100,
            height = 200
        ))
    )

    @Mock
    private lateinit var fileSystemPictureStorageService: PictureStorageService

    @BeforeEach
    fun setUp() {
        fileSystemPictureStorageService = FileSystemPictureStorageService(downloadService, resizeImageService, pictureConfiguration)
    }

    @Test
    @Throws(Exception::class)
    fun savePicture_withValidUrl_returnsPictureIdOfDownloadedFile() {
        // Arrange
        val pictureId = PictureId("1fc673b0-f1c8-4d8a-bd6c-c852fd44addf")
        val content = byteArrayOf('a'.toByte(), 'b'.toByte(), 'c'.toByte(), 'd'.toByte())
        `when`(downloadService.download(PICTURE_URL)).thenReturn(content)

        // Act
        val resultPictureId = fileSystemPictureStorageService.savePicture(PICTURE_URL, pictureId)

        // Assert
        assertThat(resultPictureId)
            .isNotNull
            .isEqualTo(pictureId)
        verify(downloadService).download(PICTURE_URL)
        val pathOfOutput: Path = Paths.get(CONF_FOLDER, pictureId.value.toString())
        assertThat(Files.readAllBytes(pathOfOutput))
            .isEqualTo(content)
        Files.delete(pathOfOutput)
    }

    @Test
    fun savePicture_withInvalidUrl_expectDownloadFailedException() {
        // Arrange
        val pictureId = PictureId("1fc673b0-f1c8-4d8a-bd6c-c852fd44adde")
        `when`(downloadService.download(ArgumentMatchers.anyString())).thenThrow(HttpClientErrorException::class.java)

        // Act
        val thrownException = ThrowingCallable { fileSystemPictureStorageService.savePicture("http://does.not.exist", pictureId) }

        // Assert
        val pathOfOutput = Paths.get(CONF_FOLDER, pictureId.value.toString())
        assertThat(pathOfOutput)
            .doesNotExist()
        assertThatThrownBy(thrownException)
            .isInstanceOf(DownloadFailedException::class.java)
    }

    @Test
    fun savePicture_withFileWriteException_expectDownloadFailedException() {
        // Arrange
        val pictureId = PictureId("1fc673b0-f1c8-4d8a-bd6c-c852fd44adda")
        val configuredFolder = "/*"
        val content = byteArrayOf('a'.toByte(), 'b'.toByte(), 'c'.toByte(), 'd'.toByte())
        `when`(downloadService.download(PICTURE_URL)).thenReturn(content)
        val originalFolder = pictureConfiguration.pictureFolder
        pictureConfiguration.pictureFolder = configuredFolder

        // Act
        val thrownException = ThrowingCallable { fileSystemPictureStorageService.savePicture(PICTURE_URL, pictureId) }

        // Assert
        assertThatThrownBy(thrownException)
            .isInstanceOf(DownloadFailedException::class.java)
        pictureConfiguration.pictureFolder = originalFolder
    }

    @Test
    @Throws(Exception::class)
    fun savePicture_withValidUrl_verifyPictureIsScaled() {
        // Arrange
        val pictureId = PictureId("1fc673b0-f1c8-4d8a-bd6c-c852fd44addf")
        val content = byteArrayOf('a'.toByte(), 'b'.toByte(), 'c'.toByte(), 'd'.toByte())
        `when`(downloadService.download(PICTURE_URL)).thenReturn(content)

        // Act
        val resultPictureId = fileSystemPictureStorageService.savePicture(PICTURE_URL, pictureId)

        // Assert
        assertThat(resultPictureId)
            .isNotNull
        val output = Paths.get(CONF_FOLDER, pictureId.value.toString())
        val pictureSize = pictureConfiguration.pictureSizes[0]
        val outputScaled = Paths.get(CONF_FOLDER, pictureSize.name, pictureId.value.toString())
        assertThat(output)
            .exists()
        verify(resizeImageService).resize(output, outputScaled, pictureSize.width, pictureSize.height, null)
        Files.delete(output)
    }

    @Test
    fun picture_withInvalidName_throwsIllegalArgumentException() {

        // Assert
        // Act
        val runnable = ThrowingCallable { fileSystemPictureStorageService.getPicture(PictureId(), "../test") }

        // Assert
        assertThatThrownBy(runnable)
            .isInstanceOf(IllegalArgumentException::class.java)
    }

    companion object {
        private const val PICTURE_URL = "https://helfenkannjeder.de/uploads/pics.jpg"
        private const val CONF_FOLDER = "target/temp/conf_folder"
    }
}