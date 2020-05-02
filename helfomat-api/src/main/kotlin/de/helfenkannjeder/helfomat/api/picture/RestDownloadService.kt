package de.helfenkannjeder.helfomat.api.picture

import de.helfenkannjeder.helfomat.core.ProfileRegistry
import de.helfenkannjeder.helfomat.core.picture.DownloadService
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

/**
 * @author Valentin Zickner
 */
@Service
@Profile("!" + ProfileRegistry.MOCK_DOWNLOAD)
class RestDownloadService(
    restTemplateBuilder: RestTemplateBuilder
) : DownloadService {

    private val restTemplate: RestTemplate = restTemplateBuilder.build()

    override fun download(url: String): ByteArray? = restTemplate.getForObject(url, ByteArray::class.java)

}