package de.helfenkannjeder.helfomat.infrastructure.filesystem

import com.google.common.base.Preconditions
import de.helfenkannjeder.helfomat.api.picture.ResizeImageService
import de.helfenkannjeder.helfomat.core.picture.DownloadFailedException
import de.helfenkannjeder.helfomat.core.picture.DownloadService
import de.helfenkannjeder.helfomat.core.picture.PictureId
import de.helfenkannjeder.helfomat.core.picture.PictureStorageService
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClientException
import java.io.IOException
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.InvalidPathException
import java.nio.file.Path
import java.nio.file.Paths
import java.util.regex.Pattern

/**
 * @author Valentin Zickner
 */
@Service
@EnableConfigurationProperties(PictureConfiguration::class)
class FileSystemPictureStorageService(
    private val downloadService: DownloadService,
    private val resizeImageService: ResizeImageService,
    private val pictureConfiguration: PictureConfiguration
) : PictureStorageService {

    @Throws(DownloadFailedException::class)
    override fun savePicture(url: String, pictureId: PictureId): PictureId {
        return try {
            val bytes = downloadService.download(url) ?: throw DownloadFailedException()
            savePicture(bytes, pictureId, null)
        } catch (exception: DownloadFailedException) {
            LOG.error("Failed to write image to filesystem url='$url' picture='$pictureId'", exception)
            throw DownloadFailedException(exception)
        } catch (exception: RestClientException) {
            LOG.error("Failed to write image to filesystem url='$url' picture='$pictureId'", exception)
            throw DownloadFailedException(exception)
        }
    }

    @Throws(DownloadFailedException::class)
    override fun savePicture(bytes: ByteArray, pictureId: PictureId, contentType: String?): PictureId {
        return try {
            val path = createPath(pictureId.value.toString())
            Files.write(path, bytes)
            scalePicture(pictureId, path, contentType)
            pictureId
        } catch (exception: IOException) {
            LOG.error("Failed to write image to filesystem picture='$pictureId'", exception)
            throw DownloadFailedException(exception)
        } catch (exception: InvalidPathException) {
            LOG.error("Failed to write image to filesystem picture='$pictureId'", exception)
            throw DownloadFailedException(exception)
        } catch (exception: RestClientException) {
            LOG.error("Failed to write image to filesystem picture='$pictureId'", exception)
            throw DownloadFailedException(exception)
        }
    }

    @Throws(DownloadFailedException::class)
    override fun savePicture(pictureId: PictureId, inputStream: InputStream, contentType: String?): PictureId {
        return try {
            val path = createPath(pictureId.value.toString())
            Files.copy(inputStream, path)
            scalePicture(pictureId, path, contentType)
            pictureId
        } catch (exception: IOException) {
            throw DownloadFailedException(exception)
        }
    }

    override fun getPicture(pictureId: PictureId): Path {
        return Paths.get(pictureConfiguration.pictureFolder, pictureId.value.toString())
    }

    override fun getPicture(pictureId: PictureId, size: String): Path {
        Preconditions.checkArgument(Pattern.compile("^[a-z\\-]+$").matcher(size).matches())
        return Paths.get(pictureConfiguration.pictureFolder, size, pictureId.value.toString())
    }

    override fun existPicture(pictureId: PictureId): Boolean {
        return Files.exists(getPicture(pictureId))
    }

    @Throws(IOException::class)
    private fun scalePicture(pictureId: PictureId, path: Path, contentType: String?) {
        for (pictureSize in pictureConfiguration.pictureSizes) {
            val outputFile = createPath(pictureSize.name, pictureId.value.toString())
            resizeImageService.resize(path, outputFile, pictureSize.width, pictureSize.height, contentType)
        }
    }

    @Throws(IOException::class)
    private fun createPath(vararg folder: String): Path {
        val path = Paths.get(pictureConfiguration.pictureFolder, *folder)
        if (!path.parent.toFile().exists()) {
            Files.createDirectories(path.parent)
        }
        return path
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(FileSystemPictureStorageService::class.java)
    }

}