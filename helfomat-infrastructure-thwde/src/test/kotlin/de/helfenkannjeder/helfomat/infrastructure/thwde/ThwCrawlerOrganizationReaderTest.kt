package de.helfenkannjeder.helfomat.infrastructure.thwde

import de.helfenkannjeder.helfomat.core.organization.Organization
import de.helfenkannjeder.helfomat.core.picture.PictureStorageService
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Offset
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class ThwCrawlerOrganizationReaderTest {
    private lateinit var thwCrawlerOrganizationReader: ThwCrawlerOrganizationReader

    @Mock
    private lateinit var pictureStorageService: PictureStorageService

    private lateinit var domain: String

    @BeforeEach
    internal fun setUp() {
        domain = "http://localhost:" + EMBEDDED_HTTP_SERVER.port + "/"
        val thwCrawlerConfiguration = ThwCrawlerConfiguration(
            domain = domain,
            isFollowDomainNames = false,
            resultsPerPage = 2,
            httpRequestTimeout = 3000
        )
        thwCrawlerOrganizationReader = ThwCrawlerOrganizationReader(
            thwCrawlerConfiguration,
            pictureStorageService
        )
    }

    @Test
    fun organizationCanBeRead() {
        val organization = thwCrawlerOrganizationReader.read()
        assertThat(organization).isNotNull
        assertThat(organization?.addresses)
            .isNotNull
            .hasSize(1)
        assertThat(organization?.addresses?.get(0)?.street)
            .isEqualTo("Eckenerstraße 52")
    }

    @Test
    fun organizationCanBeRead_withCoordinatesOfOrganization_returnsOrganizationWithCoordinates() {
        // Arrange


        // Act
        val organization = thwCrawlerOrganizationReader.read()

        // Assert
        assertThat(organization).isNotNull
        assertThat(organization?.name).isEqualTo("THW Ortsverband Aachen")
        val addresses = organization?.addresses
        assertThat(addresses)
            .isNotNull
            .hasSize(1)
        val location = addresses?.get(0)?.location
        assertThat(location).isNotNull
        assertThat(location?.lat).isEqualTo(50.756488, Offset.offset(0.0000001))
        assertThat(location?.lon).isEqualTo(6.158488, Offset.offset(0.0000001))
    }

    @Test
    fun read_executeTwice_verifyOrganizationCorrect() {
        // Act
        val organization1 = thwCrawlerOrganizationReader.read()
        val organization2 = thwCrawlerOrganizationReader.read()

        // Assert
        assertThat(organization1).isNotNull
        assertThat(organization2).isNotNull
        assertThat(organization1?.name).isEqualTo("THW Ortsverband Aachen")
        assertThat(organization2?.name).isEqualTo("THW Ortsverband Aalen")
    }

    @Test
    fun lastReadOvIsBacknang() {
        var nextOrganization: Organization? = null
        var organization: Organization?
        do {
            organization = nextOrganization
            nextOrganization = thwCrawlerOrganizationReader.read()
        } while (nextOrganization != null)
        assertThat(organization).isNotNull
        assertThat(organization?.name).isEqualTo("THW Ortsverband Backnang")
    }

    @Test
    fun read_withGroupsOfAalen_returnsCorrectListOfOrganizations() {
        // Arrange
        // Start with the second one, less complexity in group structure
        thwCrawlerOrganizationReader.read()

        // Act
        val organization = thwCrawlerOrganizationReader.read()

        // Assert
        assertThat(organization).isNotNull
        assertThat(organization?.name).isEqualTo("THW Ortsverband Aalen")
        val groups = organization?.groups
        assertThat(groups)
            .isNotNull
            .hasSize(5)
        assertThat(groups?.get(0)?.name).isEqualTo("Zugtrupp")
        assertThat(groups?.get(1)?.name).isEqualTo("Bergungsgruppe 1")
        assertThat(groups?.get(2)?.name).isEqualTo("Bergungsgruppe 2, Typ B")
        assertThat(groups?.get(3)?.name).isEqualTo("Fachgruppe Räumen Typ A (ALT)")
        assertThat(groups?.get(4)?.name).isEqualTo("Fachgruppe Ortung Typ B")
    }

    @Test
    fun read_withGroupDescriptionsOfAalen_returnsCorrectListOfOrganizations() {
        // Arrange
        // Start with the second one, less complexity in group structure
        thwCrawlerOrganizationReader.read()

        // Act
        val organization = thwCrawlerOrganizationReader.read()

        // Assert
        assertThat(organization).isNotNull
        assertThat(organization?.name).isEqualTo("THW Ortsverband Aalen")
        val groups = organization?.groups
        assertThat(groups)
            .isNotNull
            .hasSize(5)
        val group = groups?.get(0)
        assertThat(group?.name).isEqualTo("Zugtrupp")
        assertThat(group?.description).isEqualTo("An der Spitze des Technischen Zuges steht der Zugführer mit seinem Zugtrupp (ZTr).")
    }

    @Test
    fun read_withContactPerson_returnsContactPersonWithoutPicture() {
        // Arrange

        // Act
        val organization = thwCrawlerOrganizationReader.read()

        // Assert
        assertThat(organization?.contactPersons)
            .isNotNull
            .hasSize(1)
        val contactPerson = organization?.contactPersons?.get(0)
        assertThat(contactPerson?.firstname).isEqualTo("Albert")
        assertThat(contactPerson?.lastname).isEqualTo("Willekens")
        assertThat(contactPerson?.rank).isEqualTo("Ortsbeauftragter")
        assertThat(contactPerson?.telephone).isEqualTo("0241 9209336")
        assertThat(contactPerson?.picture).isNull()
        verify(pictureStorageService, Mockito.never())?.savePicture(
            eq(domain + "/SharedDocs/Bilder/DE/TiUe/NoElementPerson.jpg?__blob=thumbnail&v=5"),
            any()
        )
    }

    @Test
    fun read_withContactPersonOfPicture_verifyPictureDownloaded() {
        // Arrange
        thwCrawlerOrganizationReader.read()
        thwCrawlerOrganizationReader.read()
        thwCrawlerOrganizationReader.read()
        Mockito.reset(pictureStorageService)

        // Act
        val organization = thwCrawlerOrganizationReader.read()

        // Assert
        assertThat(organization?.contactPersons)
            .isNotNull
            .hasSize(1)
        verify(pictureStorageService)?.savePicture(
            eq(domain + "/SharedDocs/Bilder/DE/TiUe/Personen/P/probstc3.jpg?__blob=thumbnail&v=2"),
            any()
        )
    }

    @Test
    fun duplicateGroupsAreNotImported() {
        // e.g. zugtrupp is only imported once even if there is a zugtrupp for each TZ

        // Arrange

        // Act
        val organization = thwCrawlerOrganizationReader.read()

        // Assert
        assertThat(organization?.groups)
            .isNotNull
            .hasSize(9)
    }

    @Test
    fun toPicture_withSameUrls_ensureThatUuidIsStatic() {
        // Arrange

        // Act
        val pictureId1 = thwCrawlerOrganizationReader.toPictureId("dummyPicture.jpg")
        val pictureId2 = thwCrawlerOrganizationReader.toPictureId("dummyPicture.jpg")

        // Assert
        assertThat(pictureId1).isEqualTo(pictureId2)
    }

    @Test
    fun toPicture_withDifferentUrls_ensureThatUuidIsStatic() {
        // Arrange

        // Act
        val pictureId1 = thwCrawlerOrganizationReader.toPictureId("dummyPicture.jpg")
        val pictureId2 = thwCrawlerOrganizationReader.toPictureId("dummyPicture2.jpg")

        // Assert
        assertThat(pictureId1).isNotEqualTo(pictureId2)
    }
    companion object {
        private const val OVERVIEW_URL = "/DE/THW/Bundesanstalt/Dienststellen/dienststellen_node.html"
        private const val MAPVIEW_URL = "/DE/THW/Bundesanstalt/Dienststellen/Kartenansicht/kartenansicht_node.html"

        private val EMBEDDED_HTTP_SERVER = EmbeddedHttpServer()

        @BeforeAll
        @JvmStatic
        fun setUpServer() {
            EMBEDDED_HTTP_SERVER.start()
            EMBEDDED_HTTP_SERVER.setContent(OVERVIEW_URL, null, "de/thw/overview/a_3.html") // Default
            EMBEDDED_HTTP_SERVER.setContent(OVERVIEW_URL, getOverviewParameter("A", 1), "de/thw/overview/a_1.html")
            EMBEDDED_HTTP_SERVER.setContent(OVERVIEW_URL, getOverviewParameter("A", 2), "de/thw/overview/a_2.html")
            EMBEDDED_HTTP_SERVER.setContent(OVERVIEW_URL, getOverviewParameter("A", 3), "de/thw/overview/a_3.html")
            EMBEDDED_HTTP_SERVER.setContent(OVERVIEW_URL, getOverviewParameter("B", 1), "de/thw/overview/b_1.html")
            EMBEDDED_HTTP_SERVER.setContent(getOrganizationUrl("A", "Aachen_Ortsverband"), null, "de/thw/detail/thw-ov-aachen.html")
            EMBEDDED_HTTP_SERVER.setContent(getOrganizationUrl("A", "Aalen_Ortsverband"), null, "de/thw/detail/thw-ov-aalen.html")
            EMBEDDED_HTTP_SERVER.setContent(getOrganizationUrl("A", "Achern_Ortsverband"), null, "de/thw/detail/thw-ov-achern.html")
            EMBEDDED_HTTP_SERVER.setContent(getOrganizationUrl("A", "Achim_Ortsverband"), null, "de/thw/detail/thw-ov-achim.html")
            EMBEDDED_HTTP_SERVER.setContent(getOrganizationUrl("B", "Backnang_Ortsverband"), null, "de/thw/detail/thw-ov-backnang.html")
            EMBEDDED_HTTP_SERVER.setContent("/SharedDocs/Einheiten/DE/Inland/FGr-Log.html", null, "de/thw/group/fgr-log.html")
            EMBEDDED_HTTP_SERVER.setContent("/SharedDocs/Einheiten/DE/Inland/Log-Fue.html", null, "de/thw/group/log-fue.html")
            EMBEDDED_HTTP_SERVER.setContent("/SharedDocs/Einheiten/DE/Inland/Log-M.html", null, "de/thw/group/log-m.html")
            EMBEDDED_HTTP_SERVER.setContent("/SharedDocs/Einheiten/DE/Inland/Log-V.html", null, "de/thw/group/log-v.html")
            EMBEDDED_HTTP_SERVER.setContent("/SharedDocs/Einheiten/DE/Inland/TZ.html", null, "de/thw/group/tz.html")
            EMBEDDED_HTTP_SERVER.setContent("/SharedDocs/Einheiten/DE/Inland/ZTr.html", null, "de/thw/group/ztr.html")
            EMBEDDED_HTTP_SERVER.setContent("/SharedDocs/Einheiten/DE/Inland/B1.html", null, "de/thw/group/b1.html")
            EMBEDDED_HTTP_SERVER.setContent("/SharedDocs/Einheiten/DE/Inland/EGS.html", null, "de/thw/group/egs.html")
            EMBEDDED_HTTP_SERVER.setContent("/SharedDocs/Einheiten/DE/Inland/B2-A.html", null, "de/thw/group/b2.html")
            EMBEDDED_HTTP_SERVER.setContent("/SharedDocs/Einheiten/DE/Inland/B2-B.html", null, "de/thw/group/b2b.html")
            EMBEDDED_HTTP_SERVER.setContent("/SharedDocs/Einheiten/DE/Inland/FGr-E.html", null, "de/thw/group/fgr-e.html")
            EMBEDDED_HTTP_SERVER.setContent("/SharedDocs/Einheiten/DE/Inland/FGr-O-A.html", null, "de/thw/group/fgr-o-a.html")
            EMBEDDED_HTTP_SERVER.setContent("/SharedDocs/Einheiten/DE/Inland/FGr-R-A.html", null, "de/thw/group/fgr-r-a.html")
            EMBEDDED_HTTP_SERVER.setContent("/SharedDocs/Einheiten/DE/Inland/FGr-O-B.html", null, "de/thw/group/fgr-o-b.html")
            EMBEDDED_HTTP_SERVER.setContent("/SharedDocs/Einheiten/DE/Inland/FGr-W-A.html", null, "de/thw/group/fgr-w-a.html")
            EMBEDDED_HTTP_SERVER.setContent("/SharedDocs/Einheiten/DE/Inland/FGr-WP.html", null, "de/thw/group/fgr-wp.html")
            EMBEDDED_HTTP_SERVER.setContent(MAPVIEW_URL, "oeId=2000612", "de/thw/map/thw-ov-aachen.html")
            EMBEDDED_HTTP_SERVER.setContent(MAPVIEW_URL, "oeId=2000121", "de/thw/map/thw-ov-aalen.html")
            EMBEDDED_HTTP_SERVER.setContent(MAPVIEW_URL, "oeId=2000108", "de/thw/map/thw-ov-achern.html")
            EMBEDDED_HTTP_SERVER.setContent(MAPVIEW_URL, "oeId=2000429", "de/thw/map/thw-ov-achim.html")
            EMBEDDED_HTTP_SERVER.setContent(MAPVIEW_URL, "oeId=2000041", "de/thw/map/thw-ov-backnang.html")
        }

        @AfterAll
        @JvmStatic
        fun tearDownServer() {
            EMBEDDED_HTTP_SERVER.stop()
        }


        private fun getOrganizationUrl(letter: String, name: String): String {
            return "/SharedDocs/Organisationseinheiten/DE/Ortsverbaende/$letter/$name.html"
        }

        private fun getOverviewParameter(letter: String, page: Int): String {
            return "oe_plzort=PLZ+oder+Ort&" +
                "sorting=cityasc&" +
                "resultsPerPage=2&" +
                "oe_typ=ortsverbaende&" +
                "oe_umkreis=25&" +
                "letter=" + letter + "&" +
                "page=" + page
        }

    }


}