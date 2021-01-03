package de.helfenkannjeder.helfomat.infrastructure.overpass

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint
import de.helfenkannjeder.helfomat.core.organization.*
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForEntity
import java.util.*

class OverpassCrawlerOrganizationReader : OrganizationReader {

    private val restTemplate = RestTemplate()

    override val name: String = "overpass-fire-station-crawler"

    override fun read(): Organization? {
        TODO("Not yet implemented")
    }

    fun parseOrganization(overpassNode: OverpassNode): Organization.Builder {
        val name = overpassNode.tags["name"] ?: "" // TODO: handle null case
        val builder = Organization.Builder(
            id = generateOrganizationId(overpassNode),
            name = name,
            urlName = name,
            organizationType = OrganizationType.FF
        )
        val address = toAddress(overpassNode)
        if (address != null) {
            builder.addresses = mutableListOf(address)
            builder.defaultAddress = address
        }
        return builder
    }

    private fun toAddress(overpassNode: OverpassNode): Address? {
        var street = overpassNode.tags["addr:street"]
        val city = overpassNode.tags["addr:city"]
        val zipcode = overpassNode.tags["addr:postcode"]
        if (street == null || city == null || zipcode == null) {
            return null
        }
        val houseNumber = overpassNode.tags["addr:housenumber"]
        if (houseNumber != null) {
            street += " $houseNumber"
        }

        return Address(
            street = street,
            city = city,
            zipcode = zipcode,
            location = GeoPoint(overpassNode.lat, overpassNode.lon)
        )
    }

    fun readOrganizations(): OverpassResponse {
        val request = "[out:json];\n" +
            "area[\"ISO3166-1\"=\"DE\"][admin_level=2];\n" +
            "node[\"amenity\"=\"fire_station\"](area);\n" +
            "out center;"
        return restTemplate.postForEntity<OverpassResponse?>("http://overpass-api.de/api/interpreter", request, OverpassResponse::class.java)
            .body ?: throw RuntimeException("Failed to retrieve overpass result")
    }

    private fun generateOrganizationId(overpassNode: OverpassNode) =
        OrganizationId(UUID.nameUUIDFromBytes("osm-${overpassNode.id}".toByteArray()).toString())

}

data class OverpassResponse(
    val version: Double,
    val generator: String,
    val osm3s: OverpassOsm3s,
    val elements: List<OverpassNode>
)

data class OverpassOsm3s(
    val timestamp_osm_base: String,
    val timestamp_areas_base: String,
    val copyright: String
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class OverpassNode(
    val id: Long,
    val lat: Double,
    val lon: Double,
    val tags: Map<String, String>
)

fun main() {
    val overpassCrawlerOrganizationReader = OverpassCrawlerOrganizationReader()
    overpassCrawlerOrganizationReader.readOrganizations()
}