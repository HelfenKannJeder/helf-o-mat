package de.helfenkannjeder.helfomat.infrastructure.azure

import com.azure.storage.blob.BlobClient
import com.azure.storage.blob.BlobContainerClientBuilder
import com.azure.storage.common.StorageSharedKeyCredential
import de.helfenkannjeder.helfomat.api.picture.PictureStorageService
import de.helfenkannjeder.helfomat.core.ProfileRegistry
import de.helfenkannjeder.helfomat.core.picture.PictureId
import org.apache.tika.Tika
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream
import java.io.InputStream


@Primary
@Service
@Profile(ProfileRegistry.AZURE)
@EnableConfigurationProperties(AzureConfigurationProperties::class)
open class AzureBlobStoragePictureStorageService(
    azureConfigurationProperties: AzureConfigurationProperties
) : PictureStorageService {

    private val blobContainerClient = BlobContainerClientBuilder()
        .endpoint(azureConfigurationProperties.endpoint)
        .containerName(azureConfigurationProperties.containerName)
        .credential(StorageSharedKeyCredential.fromConnectionString(azureConfigurationProperties.connectionString))
        .buildClient()

    init {
        if (!blobContainerClient.exists()) {
            blobContainerClient.create()
        }
    }

    override fun savePicture(bytes: ByteArray, pictureId: PictureId, contentType: String?) {
        return savePicture(pictureId, ByteArrayInputStream(bytes), bytes.size.toLong())
    }

    override fun savePicture(pictureId: PictureId, inputStream: InputStream, fileSize: Long) {
        getBlobClient(pictureId).upload(inputStream, fileSize, true)
    }

    override fun savePicture(pictureId: PictureId, inputStream: InputStream, fileSize: Long, tag: String) {
        getBlobClient(pictureId, tag).upload(inputStream, fileSize, true)
    }

    override fun getPicture(pictureId: PictureId): InputStream {
        return getBlobClient(pictureId).openInputStream()
    }

    override fun getPicture(pictureId: PictureId, tag: String): InputStream {
        return getBlobClient(pictureId, tag).openInputStream()
    }

    override fun existPicture(pictureId: PictureId): Boolean =
        getBlobClient(pictureId).exists()

    override fun getContentType(pictureId: PictureId): String? {
        return Tika().detect(getPicture(pictureId))
    }

    fun deletePicture(pictureId: PictureId) {
        getBlobClient(pictureId).delete()
    }

    fun deletePicture(pictureId: PictureId, tag: String) {
        getBlobClient(pictureId, tag).delete()
    }

    private fun getBlobClient(pictureId: PictureId, tag: String? = null): BlobClient {
        val appendix: String = if (tag != null) "/$tag" else ""
        return blobContainerClient.getBlobClient(pictureId.value.toString() + appendix)
    }

    override fun savePicture(url: String, pictureId: PictureId) {
        throw RuntimeException("Unsupported operation")
    }

}
