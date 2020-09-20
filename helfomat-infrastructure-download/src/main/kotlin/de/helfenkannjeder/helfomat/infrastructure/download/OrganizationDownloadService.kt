package de.helfenkannjeder.helfomat.infrastructure.download

import de.helfenkannjeder.helfomat.api.organization.OrganizationDetailDto
import de.helfenkannjeder.helfomat.api.organization.OrganizationDto
import de.helfenkannjeder.helfomat.api.organization.toOrganization
import de.helfenkannjeder.helfomat.core.ProfileRegistry
import de.helfenkannjeder.helfomat.core.organization.Organization
import de.helfenkannjeder.helfomat.core.picture.DownloadFailedException
import de.helfenkannjeder.helfomat.core.picture.PictureId
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Profile
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
@Profile(ProfileRegistry.ENABLE_DOWNLOAD)
@EnableConfigurationProperties(DownloadConfigurationProperties::class)
open class OrganizationDownloadService(
    val downloadConfigurationProperties: DownloadConfigurationProperties
) {

    val restTemplate = RestTemplate() // an autowired rest template would potentially have authentication. We would like to not use authentication since all links are public

    open fun getOrganizationList(): List<Organization> {
        val api = downloadConfigurationProperties.api
        val organizationRequest = restTemplate.exchange("$api/organization/global", HttpMethod.GET, HttpEntity.EMPTY, typeReference<List<OrganizationDto>>())
        if (organizationRequest.statusCode != HttpStatus.OK) {
            throw DownloadFailedException()
        }
        return organizationRequest.body
            ?.map {
                val organization = restTemplate.exchange("$api/organization/${it.urlName}", HttpMethod.GET, HttpEntity.EMPTY, typeReference<OrganizationDetailDto>())
                return@map organization.body?.toOrganization()
            }
            ?.filterNotNull() ?: emptyList()
    }

    open fun getPicture(pictureId: PictureId): ByteArray? {
        return restTemplate.getForObject("${downloadConfigurationProperties.api}/picture/${pictureId.value}", ByteArray::class.java)
    }

    private inline fun <reified T> typeReference() = object : ParameterizedTypeReference<T>() {}

}