package de.helfenkannjeder.helfomat.rest

import de.helfenkannjeder.helfomat.api.picture.PictureStorageService
import de.helfenkannjeder.helfomat.config.ImporterConfiguration
import de.helfenkannjeder.helfomat.core.picture.DownloadFailedException
import de.helfenkannjeder.helfomat.core.picture.PictureId
import org.springframework.core.io.Resource
import org.springframework.http.*
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.client.RestTemplate
import java.io.InputStream

/**
 * @author Valentin Zickner
 */
@Component
open class RestPictureStorageService(
    private val restTemplate: RestTemplate,
    private val importerConfiguration: ImporterConfiguration
) : PictureStorageService {

    @Retryable(maxAttempts = 5, include = [RuntimeException::class], backoff = Backoff(delay = 15000, multiplier = 2.0))
    override fun savePicture(url: String, pictureId: PictureId) {
        val plainRestTemplate = RestTemplate()
        val httpEntity = HttpEntity<Any>(HttpHeaders())
        val responseEntity = plainRestTemplate.exchange(url, HttpMethod.GET, httpEntity, Resource::class.java)
        val body = responseEntity.body ?: throw DownloadFailedException()
        val inputStream = body.inputStream
        savePicture(inputStream.readAllBytes(), pictureId, null)
    }

    @Retryable(maxAttempts = 5, include = [RuntimeException::class], backoff = Backoff(delay = 15000, multiplier = 2.0))
    override fun savePicture(bytes: ByteArray, pictureId: PictureId, contentType: String?) {
        // it seems to be unnecessary hard to upload a file....
        // see https://medium.com/red6-es/uploading-a-file-with-a-filename-with-spring-resttemplate-8ec5e7dc52ca for mor details
        val fileMap: MultiValueMap<String, String> = LinkedMultiValueMap()
        val contentDisposition = ContentDisposition
            .builder("form-data")
            .name("file")
            .filename("image")
            .build()
        fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString())
        val fileEntity = HttpEntity(bytes, fileMap)
        val body: MultiValueMap<String, Any> = LinkedMultiValueMap()
        body.add("file", fileEntity)
        val headers = HttpHeaders()
        headers.contentType = MediaType.MULTIPART_FORM_DATA
        val requestEntity = HttpEntity(body, headers)
        restTemplate.exchange("${importerConfiguration.webApiUrl}/api/picture/${pictureId.value}", HttpMethod.POST, requestEntity, Void::class.java)
    }

    override fun savePicture(pictureId: PictureId, inputStream: InputStream, fileSize: Long, tag: String) {
        throw UnsupportedOperationException()
    }

    override fun savePicture(pictureId: PictureId, inputStream: InputStream, size: Long) {
        throw UnsupportedOperationException()
    }

    override fun getContentType(pictureId: PictureId): String? {
        throw UnsupportedOperationException()
    }

    @Retryable(maxAttempts = 5, include = [RuntimeException::class], backoff = Backoff(delay = 15000, multiplier = 2.0))
    override fun existPicture(pictureId: PictureId): Boolean {
        return try {
            restTemplate.headForHeaders(importerConfiguration.webApiUrl + "/api/picture/" + pictureId.value)
            true
        } catch (e: ResourceAccessException) {
            false
        }
    }

    override fun getPicture(pictureId: PictureId): InputStream {
        throw UnsupportedOperationException()
    }

    override fun getPicture(pictureId: PictureId, tag: String): InputStream {
        throw UnsupportedOperationException()
    }

}