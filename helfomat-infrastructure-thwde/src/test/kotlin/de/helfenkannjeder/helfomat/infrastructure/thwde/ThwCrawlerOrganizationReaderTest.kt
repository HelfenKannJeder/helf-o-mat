package de.helfenkannjeder.helfomat.infrastructure.thwde

import de.helfenkannjeder.helfomat.core.IndexManager
import de.helfenkannjeder.helfomat.core.organization.Organization
import de.helfenkannjeder.helfomat.core.picture.PictureStorageService
import de.helfenkannjeder.helfomat.infrastructure.thwde.EmbeddedHttpServer.setContent
import de.helfenkannjeder.helfomat.infrastructure.thwde.EmbeddedHttpServer.start
import de.helfenkannjeder.helfomat.infrastructure.thwde.EmbeddedHttpServer.stop
import org.assertj.core.api.Assertions
import org.assertj.core.data.Offset
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class ThwCrawlerOrganizationReaderTest {
    private var thwCrawlerOrganizationReader: ThwCrawlerOrganizationReader? = null

    @Mock
    private val pictureStorageService: PictureStorageService? = null

    @Mock
    private val indexManager: IndexManager? = null
    private var domain: String? = null

    @BeforeEach
    fun setUp() {
        val thwCrawlerConfiguration = ThwCrawlerConfiguration()
        domain = "http://localhost:" + EmbeddedHttpServer.PORT + "/"
        thwCrawlerConfiguration.domain = domain
        thwCrawlerConfiguration.isFollowDomainNames = false
        thwCrawlerConfiguration.resultsPerPage = 2
        thwCrawlerConfiguration.httpRequestTimeout = 3000
        thwCrawlerOrganizationReader = ThwCrawlerOrganizationReader(
            thwCrawlerConfiguration,
            pictureStorageService!!
        )
    }

    @Test
    fun organizationCanBeRead() {
        val organization = thwCrawlerOrganizationReader!!.read()
        Assertions.assertThat(organization!!.addresses)
            .isNotNull
            .hasSize(1)
        Assertions.assertThat(organization.addresses[0].street)
            .isEqualTo("Eckenerstraße 52")
    }

    @Test
    fun organizationCanBeRead_withCoordinatesOfOrganization_returnsOrganizationWithCoordinates() {
        // Arrange


        // Act
        val organization = thwCrawlerOrganizationReader!!.read()

        // Assert
        Assertions.assertThat(organization).isNotNull
        Assertions.assertThat(organization!!.name).isEqualTo("THW Ortsverband Aachen")
        val addresses = organization.addresses
        Assertions.assertThat(addresses)
            .isNotNull
            .hasSize(1)
        val location = addresses[0].location
        Assertions.assertThat(location).isNotNull
        Assertions.assertThat(location.lat).isEqualTo(50.756488, Offset.offset(0.0000001))
        Assertions.assertThat(location.lon).isEqualTo(6.158488, Offset.offset(0.0000001))
    }

    @Test
    fun read_executeTwice_verifyOrganizationCorrect() {
        // Act
        val organization1 = thwCrawlerOrganizationReader!!.read()
        val organization2 = thwCrawlerOrganizationReader!!.read()

        // Assert
        Assertions.assertThat(organization1).isNotNull
        Assertions.assertThat(organization2).isNotNull
        Assertions.assertThat(organization1!!.name).isEqualTo("THW Ortsverband Aachen")
        Assertions.assertThat(organization2!!.name).isEqualTo("THW Ortsverband Aalen")
    }

    @Test
    fun lastReadOvIsBacknang() {
        var nextOrganization: Organization? = null
        var organization: Organization?
        do {
            organization = nextOrganization
            nextOrganization = thwCrawlerOrganizationReader!!.read()
        } while (nextOrganization != null)
        Assertions.assertThat(organization).isNotNull
        Assertions.assertThat(organization!!.name).isEqualTo("THW Ortsverband Backnang")
    }

    @Test
    fun read_withGroupsOfAalen_returnsCorrectListOfOrganizations() {
        // Arrange
        // Start with the second one, less complexity in group structure
        thwCrawlerOrganizationReader!!.read()

        // Act
        val organization = thwCrawlerOrganizationReader!!.read()

        // Assert
        Assertions.assertThat(organization).isNotNull
        Assertions.assertThat(organization!!.name).isEqualTo("THW Ortsverband Aalen")
        val groups = organization.groups
        Assertions.assertThat(groups)
            .isNotNull
            .hasSize(5)
        Assertions.assertThat(groups[0].name).isEqualTo("Zugtrupp")
        Assertions.assertThat(groups[1].name).isEqualTo("Bergungsgruppe 1")
        Assertions.assertThat(groups[2].name).isEqualTo("Bergungsgruppe 2, Typ B")
        Assertions.assertThat(groups[3].name).isEqualTo("Fachgruppe Räumen Typ A (ALT)")
        Assertions.assertThat(groups[4].name).isEqualTo("Fachgruppe Ortung Typ B")
    }

    @Test
    fun read_withGroupDescriptionsOfAalen_returnsCorrectListOfOrganizations() {
        // Arrange
        // Start with the second one, less complexity in group structure
        thwCrawlerOrganizationReader!!.read()

        // Act
        val organization = thwCrawlerOrganizationReader!!.read()

        // Assert
        Assertions.assertThat(organization).isNotNull
        Assertions.assertThat(organization!!.name).isEqualTo("THW Ortsverband Aalen")
        val groups = organization.groups
        Assertions.assertThat(groups)
            .isNotNull
            .hasSize(5)
        val group = groups[0]
        Assertions.assertThat(group.name).isEqualTo("Zugtrupp")
        Assertions.assertThat(group.description).isEqualTo("An der Spitze des Technischen Zuges steht der Zugführer mit seinem Zugtrupp (ZTr).")
    }

    @Test
    fun read_withContactPerson_returnsContactPersonWithoutPicture() {
        // Arrange

        // Act
        val organization = thwCrawlerOrganizationReader!!.read()

        // Assert
        Assertions.assertThat(organization!!.contactPersons)
            .isNotNull
            .hasSize(1)
        val contactPerson = organization.contactPersons[0]
        Assertions.assertThat(contactPerson.firstname).isEqualTo("Albert")
        Assertions.assertThat(contactPerson.lastname).isEqualTo("Willekens")
        Assertions.assertThat(contactPerson.rank).isEqualTo("Ortsbeauftragter")
        Assertions.assertThat(contactPerson.telephone).isEqualTo("0241 9209336")
        Assertions.assertThat(contactPerson.picture).isNull()
        Mockito.verify(pictureStorageService, Mockito.never())?.savePicture(
            ArgumentMatchers.eq(domain + "/SharedDocs/Bilder/DE/TiUe/NoElementPerson.jpg?__blob=thumbnail&v=5"),
            ArgumentMatchers.any()
        )
    }

    @Test
    fun read_withContactPersonOfPicture_verifyPictureDownloaded() {
        // Arrange
        thwCrawlerOrganizationReader!!.read()
        thwCrawlerOrganizationReader!!.read()
        thwCrawlerOrganizationReader!!.read()
        Mockito.reset(pictureStorageService)

        // Act
        val organization = thwCrawlerOrganizationReader!!.read()

        // Assert
        Assertions.assertThat(organization!!.contactPersons)
            .isNotNull
            .hasSize(1)
        Mockito.verify(pictureStorageService)?.savePicture(
            ArgumentMatchers.eq(domain + "/SharedDocs/Bilder/DE/TiUe/Personen/P/probstc3.jpg?__blob=thumbnail&v=2"),
            ArgumentMatchers.any()
        )
    }

    @Test
    fun duplicateGroupsAreNotImported() {
        // e.g. zugtrupp is only imported once even if there is a zugtrupp for each TZ

        // Arrange

        // Act
        val organization = thwCrawlerOrganizationReader!!.read()

        // Assert
        Assertions.assertThat(organization!!.groups)
            .isNotNull
            .hasSize(9)
    }

    @Test
    fun toPicture_withSameUrls_ensureThatUuidIsStatic() {
        // Arrange

        // Act
        val pictureId1 = thwCrawlerOrganizationReader!!.toPictureId("dummyPicture.jpg")
        val pictureId2 = thwCrawlerOrganizationReader!!.toPictureId("dummyPicture.jpg")

        // Assert
        Assertions.assertThat(pictureId1).isEqualTo(pictureId2)
    }

    @Test
    fun toPicture_withDifferentUrls_ensureThatUuidIsStatic() {
        // Arrange

        // Act
        val pictureId1 = thwCrawlerOrganizationReader!!.toPictureId("dummyPicture.jpg")
        val pictureId2 = thwCrawlerOrganizationReader!!.toPictureId("dummyPicture2.jpg")

        // Assert
        Assertions.assertThat(pictureId1).isNotEqualTo(pictureId2)
    }
    companion object {
        private const val OVERVIEW_URL = "/DE/THW/Bundesanstalt/Dienststellen/dienststellen_node.html"
        private const val MAPVIEW_URL = "/DE/THW/Bundesanstalt/Dienststellen/Kartenansicht/kartenansicht_node.html"

        @BeforeAll
        @JvmStatic
        @Throws(Exception::class)
        fun setUpServer() {
            start()
            setContent(OVERVIEW_URL, null, "de/thw/overview/a_3.html") // Default
            setContent(OVERVIEW_URL, getOverviewParameter("A", 1), "de/thw/overview/a_1.html")
            setContent(OVERVIEW_URL, getOverviewParameter("A", 2), "de/thw/overview/a_2.html")
            setContent(OVERVIEW_URL, getOverviewParameter("A", 3), "de/thw/overview/a_3.html")
            setContent(OVERVIEW_URL, getOverviewParameter("B", 1), "de/thw/overview/b_1.html")
            setContent(getOrganizationUrl("A", "Aachen_Ortsverband"), null, "de/thw/detail/thw-ov-aachen.html")
            setContent(getOrganizationUrl("A", "Aalen_Ortsverband"), null, "de/thw/detail/thw-ov-aalen.html")
            setContent(getOrganizationUrl("A", "Achern_Ortsverband"), null, "de/thw/detail/thw-ov-achern.html")
            setContent(getOrganizationUrl("A", "Achim_Ortsverband"), null, "de/thw/detail/thw-ov-achim.html")
            setContent(getOrganizationUrl("B", "Backnang_Ortsverband"), null, "de/thw/detail/thw-ov-backnang.html")
            setContent("/SharedDocs/Einheiten/DE/Inland/FGr-Log.html", null, "de/thw/group/fgr-log.html")
            setContent("/SharedDocs/Einheiten/DE/Inland/Log-Fue.html", null, "de/thw/group/log-fue.html")
            setContent("/SharedDocs/Einheiten/DE/Inland/Log-M.html", null, "de/thw/group/log-m.html")
            setContent("/SharedDocs/Einheiten/DE/Inland/Log-V.html", null, "de/thw/group/log-v.html")
            setContent("/SharedDocs/Einheiten/DE/Inland/TZ.html", null, "de/thw/group/tz.html")
            setContent("/SharedDocs/Einheiten/DE/Inland/ZTr.html", null, "de/thw/group/ztr.html")
            setContent("/SharedDocs/Einheiten/DE/Inland/B1.html", null, "de/thw/group/b1.html")
            setContent("/SharedDocs/Einheiten/DE/Inland/EGS.html", null, "de/thw/group/egs.html")
            setContent("/SharedDocs/Einheiten/DE/Inland/B2-A.html", null, "de/thw/group/b2.html")
            setContent("/SharedDocs/Einheiten/DE/Inland/B2-B.html", null, "de/thw/group/b2b.html")
            setContent("/SharedDocs/Einheiten/DE/Inland/FGr-E.html", null, "de/thw/group/fgr-e.html")
            setContent("/SharedDocs/Einheiten/DE/Inland/FGr-O-A.html", null, "de/thw/group/fgr-o-a.html")
            setContent("/SharedDocs/Einheiten/DE/Inland/FGr-R-A.html", null, "de/thw/group/fgr-r-a.html")
            setContent("/SharedDocs/Einheiten/DE/Inland/FGr-O-B.html", null, "de/thw/group/fgr-o-b.html")
            setContent("/SharedDocs/Einheiten/DE/Inland/FGr-W-A.html", null, "de/thw/group/fgr-w-a.html")
            setContent("/SharedDocs/Einheiten/DE/Inland/FGr-WP.html", null, "de/thw/group/fgr-wp.html")
            setContent(MAPVIEW_URL, "oeId=2000612", "de/thw/map/thw-ov-aachen.html")
            setContent(MAPVIEW_URL, "oeId=2000121", "de/thw/map/thw-ov-aalen.html")
            setContent(MAPVIEW_URL, "oeId=2000108", "de/thw/map/thw-ov-achern.html")
            setContent(MAPVIEW_URL, "oeId=2000429", "de/thw/map/thw-ov-achim.html")
            setContent(MAPVIEW_URL, "oeId=2000041", "de/thw/map/thw-ov-backnang.html")
        }

        @AfterAll
        @JvmStatic
        fun tearDownServer() {
            stop()
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