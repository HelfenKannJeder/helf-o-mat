package de.helfenkannjeder.helfomat.infrastructure.filesystem

import com.google.common.base.Preconditions
import de.helfenkannjeder.helfomat.api.picture.PictureStorageService
import de.helfenkannjeder.helfomat.core.picture.DownloadFailedException
import de.helfenkannjeder.helfomat.core.picture.DownloadService
import de.helfenkannjeder.helfomat.core.picture.PictureId
import org.apache.tika.Tika
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Service
import java.io.IOException
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.regex.Pattern

/**
 * @author Valentin Zickner
 */
@Service
@EnableConfigurationProperties(FileSystemPictureConfigurationProperties::class)
class FileSystemPictureStorageService(
    private val downloadService: DownloadService,
    private val fileSystemPictureConfigurationProperties: FileSystemPictureConfigurationProperties
) : PictureStorageService {

    override fun savePicture(url: String, pictureId: PictureId) {
        val bytes = downloadService.download(url) ?: throw DownloadFailedException()
        savePicture(bytes, pictureId, null)
    }

    override fun savePicture(bytes: ByteArray, pictureId: PictureId, contentType: String?) {
        val path = createPath(pictureId.value.toString())
        Files.write(path, bytes)
    }

    override fun savePicture(pictureId: PictureId, inputStream: InputStream, fileSize: Long) {
        val path = createPath(pictureId.value.toString())
        Files.copy(inputStream, path)
    }

    override fun savePicture(pictureId: PictureId, inputStream: InputStream, fileSize: Long, tag: String) {
        val path = createPath(tag, pictureId.value.toString())
        Files.copy(inputStream, path)
    }

    override fun getContentType(pictureId: PictureId): String? = Tika().detect(createPath(pictureId.value.toString()))

    override fun getPicture(pictureId: PictureId): InputStream {
        return Files.newInputStream(getPicturePath(pictureId))
    }

    override fun getPicture(pictureId: PictureId, tag: String): InputStream {
        Preconditions.checkArgument(Pattern.compile("^[a-z\\-]+$").matcher(tag).matches())
        return Files.newInputStream(Paths.get(fileSystemPictureConfigurationProperties.pictureFolder, tag, pictureId.value.toString()))
    }

    override fun existPicture(pictureId: PictureId): Boolean = Files.exists(getPicturePath(pictureId))

    private fun getPicturePath(pictureId: PictureId) = Paths.get(fileSystemPictureConfigurationProperties.pictureFolder, pictureId.value.toString())

    @Throws(IOException::class)
    private fun createPath(vararg folder: String): Path {
        val path = Paths.get(fileSystemPictureConfigurationProperties.pictureFolder, *folder)
        if (!path.parent.toFile().exists()) {
            Files.createDirectories(path.parent)
        }
        return path
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(FileSystemPictureStorageService::class.java)
    }

}