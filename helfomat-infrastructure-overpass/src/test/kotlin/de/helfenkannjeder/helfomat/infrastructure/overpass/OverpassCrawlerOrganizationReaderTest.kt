package de.helfenkannjeder.helfomat.infrastructure.overpass

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import de.helfenkannjeder.helfomat.core.organization.OrganizationId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.core.io.ClassPathResource
import org.springframework.util.StreamUtils
import java.nio.charset.Charset

internal class OverpassCrawlerOrganizationReaderTest {

    private val objectMapper = ObjectMapper()
        .registerModule(KotlinModule())

    private val overpassCrawlerOrganizationReader = OverpassCrawlerOrganizationReader()

    @Test
    fun convertToOrganization_withSingleOrganization_ensureInformationIsMapped() {
        // Arrange
        val overpassNode = readOverpassNode("overpass-api-response-fire-station-bohnsdorf.json")

        // Act
        val builder = overpassCrawlerOrganizationReader.parseOrganization(overpassNode)

        // Assert
        val organization = builder.build()
        assertThat(organization.id).isEqualTo(OrganizationId("a5e866ad-da03-3cab-b5b5-fb85b5c16ea8"))
        assertThat(organization.name).isEqualTo("Freiwillige Feuerwehr Bohnsdorf")
        assertThat(organization.urlName).isEqualTo("Freiwillige Feuerwehr Bohnsdorf")
        assertThat(organization.defaultAddress).isNotNull
        assertThat(organization.defaultAddress?.street).isEqualTo("Waltersdorfer Straße 107")
        assertThat(organization.defaultAddress?.city).isEqualTo("Berlin")
        assertThat(organization.defaultAddress?.zipcode).isEqualTo("12526")
        assertThat(organization.defaultAddress?.location).isNotNull
        assertThat(organization.defaultAddress?.location?.lat).isEqualTo(52.4016830)
        assertThat(organization.defaultAddress?.location?.lon).isEqualTo(13.5743044)
    }

    private fun readOverpassNode(filename: String): OverpassNode = objectMapper.readValue(readJson(filename))

    private fun readJson(filename: String): String = StreamUtils.copyToString(ClassPathResource(filename).inputStream, Charset.defaultCharset())

}