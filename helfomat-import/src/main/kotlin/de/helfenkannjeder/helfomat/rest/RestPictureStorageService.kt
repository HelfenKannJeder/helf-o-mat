package de.helfenkannjeder.helfomat.rest

import de.helfenkannjeder.helfomat.config.ImporterConfiguration
import de.helfenkannjeder.helfomat.core.picture.DownloadFailedException
import de.helfenkannjeder.helfomat.core.picture.PictureId
import de.helfenkannjeder.helfomat.core.picture.PictureStorageService
import org.springframework.core.io.Resource
import org.springframework.http.*
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import java.io.IOException
import java.io.InputStream
import java.nio.file.Path

/**
 * @author Valentin Zickner
 */
@Component
class RestPictureStorageService(
    private val restTemplate: RestTemplate,
    private val importerConfiguration: ImporterConfiguration
) : PictureStorageService {

    @Throws(DownloadFailedException::class)
    override fun savePicture(url: String, pictureId: PictureId): PictureId {
        val plainRestTemplate = RestTemplate()
        return try {
            val httpEntity = HttpEntity<Any>(HttpHeaders())
            val responseEntity = plainRestTemplate.exchange(url, HttpMethod.GET, httpEntity, Resource::class.java)
            val body = responseEntity.body ?: throw DownloadFailedException()
            val inputStream = body.inputStream
            savePicture(inputStream.readAllBytes(), pictureId)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    @Throws(DownloadFailedException::class)
    override fun savePicture(bytes: ByteArray, pictureId: PictureId): PictureId {
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
        return pictureId
    }

    override fun savePicture(pictureId: PictureId, inputStream: InputStream): PictureId {
        throw UnsupportedOperationException()
    }

    override fun existPicture(pictureId: PictureId): Boolean {
        return try {
            restTemplate.headForHeaders(importerConfiguration.webApiUrl + "/api/picture/" + pictureId.value)
            true
        } catch (restClientException: HttpClientErrorException) {
            false
        }
    }

    override fun getPicture(pictureId: PictureId): Path {
        throw UnsupportedOperationException()
    }

    override fun getPicture(pictureId: PictureId, size: String): Path {
        throw UnsupportedOperationException()
    }

}