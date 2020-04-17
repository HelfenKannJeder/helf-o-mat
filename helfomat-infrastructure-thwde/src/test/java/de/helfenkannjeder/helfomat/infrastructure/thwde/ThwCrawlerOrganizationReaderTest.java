package de.helfenkannjeder.helfomat.infrastructure.thwde;

import de.helfenkannjeder.helfomat.core.IndexManager;
import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint;
import de.helfenkannjeder.helfomat.core.organization.Address;
import de.helfenkannjeder.helfomat.core.organization.ContactPerson;
import de.helfenkannjeder.helfomat.core.organization.Group;
import de.helfenkannjeder.helfomat.core.organization.Organization;
import de.helfenkannjeder.helfomat.core.picture.PictureId;
import de.helfenkannjeder.helfomat.core.picture.PictureRepository;
import org.assertj.core.data.Offset;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ThwCrawlerOrganizationReaderTest {

    private static final String OVERVIEW_URL = "/DE/THW/Bundesanstalt/Dienststellen/dienststellen_node.html";
    private static final String MAPVIEW_URL = "/DE/THW/Bundesanstalt/Dienststellen/Kartenansicht/kartenansicht_node.html";
    private ThwCrawlerOrganizationReader thwCrawlerOrganizationReader;

    @Mock
    private PictureRepository pictureRepository;

    @Mock
    private IndexManager indexManager;

    private String domain;

    @BeforeClass
    public static void setUpServer() throws Exception {
        EmbeddedHttpServer.start();
        EmbeddedHttpServer.setContent(OVERVIEW_URL, null, "de/thw/overview/a_3.html"); // Default
        EmbeddedHttpServer.setContent(OVERVIEW_URL, getOverviewParameter("A", 1), "de/thw/overview/a_1.html");
        EmbeddedHttpServer.setContent(OVERVIEW_URL, getOverviewParameter("A", 2), "de/thw/overview/a_2.html");
        EmbeddedHttpServer.setContent(OVERVIEW_URL, getOverviewParameter("A", 3), "de/thw/overview/a_3.html");
        EmbeddedHttpServer.setContent(OVERVIEW_URL, getOverviewParameter("B", 1), "de/thw/overview/b_1.html");
        EmbeddedHttpServer.setContent(getOrganizationUrl("A", "Aachen_Ortsverband"), null, "de/thw/detail/thw-ov-aachen.html");
        EmbeddedHttpServer.setContent(getOrganizationUrl("A", "Aalen_Ortsverband"), null, "de/thw/detail/thw-ov-aalen.html");
        EmbeddedHttpServer.setContent(getOrganizationUrl("A", "Achern_Ortsverband"), null, "de/thw/detail/thw-ov-achern.html");
        EmbeddedHttpServer.setContent(getOrganizationUrl("A", "Achim_Ortsverband"), null, "de/thw/detail/thw-ov-achim.html");
        EmbeddedHttpServer.setContent(getOrganizationUrl("B", "Backnang_Ortsverband"), null, "de/thw/detail/thw-ov-backnang.html");

        EmbeddedHttpServer.setContent("/SharedDocs/Einheiten/DE/Inland/FGr-Log.html", null, "de/thw/group/fgr-log.html");
        EmbeddedHttpServer.setContent("/SharedDocs/Einheiten/DE/Inland/Log-Fue.html", null, "de/thw/group/log-fue.html");
        EmbeddedHttpServer.setContent("/SharedDocs/Einheiten/DE/Inland/Log-M.html", null, "de/thw/group/log-m.html");
        EmbeddedHttpServer.setContent("/SharedDocs/Einheiten/DE/Inland/Log-V.html", null, "de/thw/group/log-v.html");
        EmbeddedHttpServer.setContent("/SharedDocs/Einheiten/DE/Inland/TZ.html", null, "de/thw/group/tz.html");
        EmbeddedHttpServer.setContent("/SharedDocs/Einheiten/DE/Inland/ZTr.html", null, "de/thw/group/ztr.html");
        EmbeddedHttpServer.setContent("/SharedDocs/Einheiten/DE/Inland/B1.html", null, "de/thw/group/b1.html");
        EmbeddedHttpServer.setContent("/SharedDocs/Einheiten/DE/Inland/EGS.html", null, "de/thw/group/egs.html");
        EmbeddedHttpServer.setContent("/SharedDocs/Einheiten/DE/Inland/B2-A.html", null, "de/thw/group/b2.html");
        EmbeddedHttpServer.setContent("/SharedDocs/Einheiten/DE/Inland/B2-B.html", null, "de/thw/group/b2b.html");
        EmbeddedHttpServer.setContent("/SharedDocs/Einheiten/DE/Inland/FGr-E.html", null, "de/thw/group/fgr-e.html");
        EmbeddedHttpServer.setContent("/SharedDocs/Einheiten/DE/Inland/FGr-O-A.html", null, "de/thw/group/fgr-o-a.html");
        EmbeddedHttpServer.setContent("/SharedDocs/Einheiten/DE/Inland/FGr-R-A.html", null, "de/thw/group/fgr-r-a.html");
        EmbeddedHttpServer.setContent("/SharedDocs/Einheiten/DE/Inland/FGr-O-B.html", null, "de/thw/group/fgr-o-b.html");
        EmbeddedHttpServer.setContent("/SharedDocs/Einheiten/DE/Inland/FGr-W-A.html", null, "de/thw/group/fgr-w-a.html");
        EmbeddedHttpServer.setContent("/SharedDocs/Einheiten/DE/Inland/FGr-WP.html", null, "de/thw/group/fgr-wp.html");

        EmbeddedHttpServer.setContent(MAPVIEW_URL, "oeId=2000612", "de/thw/map/thw-ov-aachen.html");
        EmbeddedHttpServer.setContent(MAPVIEW_URL, "oeId=2000121", "de/thw/map/thw-ov-aalen.html");
        EmbeddedHttpServer.setContent(MAPVIEW_URL, "oeId=2000108", "de/thw/map/thw-ov-achern.html");
        EmbeddedHttpServer.setContent(MAPVIEW_URL, "oeId=2000429", "de/thw/map/thw-ov-achim.html");
        EmbeddedHttpServer.setContent(MAPVIEW_URL, "oeId=2000041", "de/thw/map/thw-ov-backnang.html");
    }

    @AfterClass
    public static void tearDownServer() {
        EmbeddedHttpServer.stop();
    }

    @Before
    public void setUp() throws Exception {
        ThwCrawlerConfiguration thwCrawlerConfiguration = new ThwCrawlerConfiguration();
        this.domain = "http://localhost:" + EmbeddedHttpServer.PORT + "/";
        thwCrawlerConfiguration.setDomain(this.domain);
        thwCrawlerConfiguration.setFollowDomainNames(false);
        thwCrawlerConfiguration.setResultsPerPage(2);
        thwCrawlerConfiguration.setHttpRequestTimeout(3000);
        thwCrawlerOrganizationReader = new ThwCrawlerOrganizationReader(
            thwCrawlerConfiguration,
            pictureRepository,
            indexManager
        );
    }

    private static String getOrganizationUrl(final String letter, final String name) {
        return "/SharedDocs/Organisationseinheiten/DE/Ortsverbaende/" + letter + "/" + name + ".html";
    }

    private static String getOverviewParameter(final String letter, final int page) {
        return "oe_plzort=PLZ+oder+Ort&" +
            "sorting=cityasc&" +
            "resultsPerPage=2&" +
            "oe_typ=ortsverbaende&" +
            "oe_umkreis=25&" +
            "letter=" + letter + "&" +
            "page=" + page;
    }

    @Test
    public void organizationCanBeRead() throws Exception {
        Organization organization = thwCrawlerOrganizationReader.read();
        assertThat(organization.getAddresses())
            .isNotNull()
            .hasSize(1);
        assertThat(organization.getAddresses().get(0).getStreet())
            .isEqualTo("Eckenerstraße 52");
    }

    @Test
    public void organizationCanBeRead_withCoordinatesOfOrganization_returnsOrganizationWithCoordinates() throws Exception {
        // Arrange


        // Act
        Organization organization = thwCrawlerOrganizationReader.read();

        // Assert
        assertThat(organization).isNotNull();
        assertThat(organization.getName()).isEqualTo("THW Ortsverband Aachen");
        List<Address> addresses = organization.getAddresses();
        assertThat(addresses)
            .isNotNull()
            .hasSize(1);
        GeoPoint location = addresses.get(0).getLocation();
        assertThat(location).isNotNull();
        assertThat(location.getLat()).isEqualTo(50.756488, Offset.offset(0.0000001));
        assertThat(location.getLon()).isEqualTo(6.158488, Offset.offset(0.0000001));
    }

    @Test
    public void read_executeTwice_verifyOrganizationCorrect() throws Exception {
        // Act
        Organization organization1 = thwCrawlerOrganizationReader.read();
        Organization organization2 = thwCrawlerOrganizationReader.read();

        // Assert
        assertThat(organization1).isNotNull();
        assertThat(organization2).isNotNull();
        assertThat(organization1.getName()).isEqualTo("THW Ortsverband Aachen");
        assertThat(organization2.getName()).isEqualTo("THW Ortsverband Aalen");
    }

    @Test
    public void lastReadOvIsBacknang() throws Exception {
        Organization nextOrganization = null;
        Organization organization;
        do {
            organization = nextOrganization;
            nextOrganization = thwCrawlerOrganizationReader.read();
        } while (nextOrganization != null);

        assertThat(organization).isNotNull();
        assertThat(organization.getName()).isEqualTo("THW Ortsverband Backnang");
    }

    @Test
    public void read_withGroupsOfAalen_returnsCorrectListOfOrganizations() throws Exception {
        // Arrange
        // Start with the second one, less complexity in group structure
        thwCrawlerOrganizationReader.read();

        // Act
        Organization organization = thwCrawlerOrganizationReader.read();

        // Assert
        assertThat(organization).isNotNull();
        assertThat(organization.getName()).isEqualTo("THW Ortsverband Aalen");
        List<Group> groups = organization.getGroups();
        assertThat(groups)
            .isNotNull()
            .hasSize(5);
        assertThat(groups.get(0).getName()).isEqualTo("Zugtrupp");
        assertThat(groups.get(1).getName()).isEqualTo("Bergungsgruppe 1");
        assertThat(groups.get(2).getName()).isEqualTo("Bergungsgruppe 2, Typ B");
        assertThat(groups.get(3).getName()).isEqualTo("Fachgruppe Räumen Typ A (ALT)");
        assertThat(groups.get(4).getName()).isEqualTo("Fachgruppe Ortung Typ B");
    }

    @Test
    public void read_withGroupDescriptionsOfAalen_returnsCorrectListOfOrganizations() throws Exception {
        // Arrange
        // Start with the second one, less complexity in group structure
        thwCrawlerOrganizationReader.read();

        // Act
        Organization organization = thwCrawlerOrganizationReader.read();

        // Assert
        assertThat(organization).isNotNull();
        assertThat(organization.getName()).isEqualTo("THW Ortsverband Aalen");
        List<Group> groups = organization.getGroups();
        assertThat(groups)
            .isNotNull()
            .hasSize(5);
        Group group = groups.get(0);
        assertThat(group.getName()).isEqualTo("Zugtrupp");
        assertThat(group.getDescription()).isEqualTo("An der Spitze des Technischen Zuges steht der Zugführer mit seinem Zugtrupp (ZTr).");
    }

    @Test
    public void read_withContactPerson_returnsContactPersonWithoutPicture() throws Exception {
        // Arrange

        // Act
        Organization organization = thwCrawlerOrganizationReader.read();

        // Assert
        assertThat(organization.getContactPersons())
            .isNotNull()
            .hasSize(1);
        ContactPerson contactPerson = organization.getContactPersons().get(0);
        assertThat(contactPerson.getFirstname()).isEqualTo("Albert");
        assertThat(contactPerson.getLastname()).isEqualTo("Willekens");
        assertThat(contactPerson.getRank()).isEqualTo("Ortsbeauftragter");
        assertThat(contactPerson.getTelephone()).isEqualTo("0241 9209336");
        assertThat(contactPerson.getPicture()).isNull();
        verify(this.pictureRepository, never()).savePicture(
            eq(this.domain + "/SharedDocs/Bilder/DE/TiUe/NoElementPerson.jpg?__blob=thumbnail&v=5"),
            anyString(),
            any()
        );
    }

    @Test
    public void read_withContactPersonOfPicture_verifyPictureDownloaded() throws Exception {
        // Arrange
        thwCrawlerOrganizationReader.read();
        thwCrawlerOrganizationReader.read();
        thwCrawlerOrganizationReader.read();
        Mockito.reset(this.pictureRepository);

        // Act
        Organization organization = thwCrawlerOrganizationReader.read();

        // Assert
        assertThat(organization.getContactPersons())
            .isNotNull()
            .hasSize(1);
        verify(this.pictureRepository).savePicture(
            eq(this.domain + "/SharedDocs/Bilder/DE/TiUe/Personen/P/probstc3.jpg?__blob=thumbnail&v=2"),
            eq(null),
            any()
        );
    }

    @Test
    public void duplicateGroupsAreNotImported() throws Exception {
        // e.g. zugtrupp is only imported once even if there is a zugtrupp for each TZ

        // Arrange

        // Act
        Organization organization = thwCrawlerOrganizationReader.read();

        // Assert
        assertThat(organization.getGroups())
            .isNotNull()
            .hasSize(9);
    }

    @Test
    public void toPicture_withSameUrls_ensureThatUuidIsStatic() {
        // Arrange

        // Act
        PictureId pictureId1 = thwCrawlerOrganizationReader.toPictureId("dummyPicture.jpg");
        PictureId pictureId2 = thwCrawlerOrganizationReader.toPictureId("dummyPicture.jpg");

        // Assert
        assertThat(pictureId1).isEqualTo(pictureId2);
    }

    @Test
    public void toPicture_withDifferentUrls_ensureThatUuidIsStatic() {
        // Arrange

        // Act
        PictureId pictureId1 = thwCrawlerOrganizationReader.toPictureId("dummyPicture.jpg");
        PictureId pictureId2 = thwCrawlerOrganizationReader.toPictureId("dummyPicture2.jpg");

        // Assert
        assertThat(pictureId1).isNotEqualTo(pictureId2);
    }

}
