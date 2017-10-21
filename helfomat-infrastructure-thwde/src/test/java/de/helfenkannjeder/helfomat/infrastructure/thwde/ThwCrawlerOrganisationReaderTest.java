package de.helfenkannjeder.helfomat.infrastructure.thwde;

import de.helfekannjeder.helfomat.core.IndexManager;
import de.helfekannjeder.helfomat.core.organisation.Address;
import de.helfekannjeder.helfomat.core.organisation.GeoPoint;
import de.helfekannjeder.helfomat.core.organisation.Group;
import de.helfekannjeder.helfomat.core.organisation.Organisation;
import de.helfekannjeder.helfomat.core.picture.PictureRepository;
import org.assertj.core.data.Offset;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ThwCrawlerOrganisationReaderTest {

    private static final String OVERVIEW_URL = "/DE/THW/Bundesanstalt/Dienststellen/dienststellen_node.html";
    private static final String MAPVIEW_URL = "/DE/THW/Bundesanstalt/Dienststellen/Kartenansicht/kartenansicht_node.html";
    private ThwCrawlerOrganisationReader thwCrawlerOrganisationReader;

    @Mock
    private PictureRepository pictureRepository;

    @Mock
    private IndexManager indexManager;

    @BeforeClass
    public static void setUpServer() throws Exception {
        EmbeddedHttpServer.start();
        EmbeddedHttpServer.setContent(OVERVIEW_URL, null, "de/thw/overview/a_3.html"); // Default
        EmbeddedHttpServer.setContent(OVERVIEW_URL, getOverviewParameter("A", 1), "de/thw/overview/a_1.html");
        EmbeddedHttpServer.setContent(OVERVIEW_URL, getOverviewParameter("A", 2), "de/thw/overview/a_2.html");
        EmbeddedHttpServer.setContent(OVERVIEW_URL, getOverviewParameter("A", 3), "de/thw/overview/a_3.html");
        EmbeddedHttpServer.setContent(OVERVIEW_URL, getOverviewParameter("B", 1), "de/thw/overview/b_1.html");
        EmbeddedHttpServer.setContent(getOrganisationUrl("A", "Aachen_Ortsverband"), null, "de/thw/detail/thw-ov-aachen.html");
        EmbeddedHttpServer.setContent(getOrganisationUrl("A", "Aalen_Ortsverband"), null, "de/thw/detail/thw-ov-aalen.html");
        EmbeddedHttpServer.setContent(getOrganisationUrl("A", "Achern_Ortsverband"), null, "de/thw/detail/thw-ov-achern.html");
        EmbeddedHttpServer.setContent(getOrganisationUrl("A", "Achim_Ortsverband"), null, "de/thw/detail/thw-ov-achim.html");
        EmbeddedHttpServer.setContent(getOrganisationUrl("B", "Backnang_Ortsverband"), null, "de/thw/detail/thw-ov-backnang.html");

        EmbeddedHttpServer.setContent(MAPVIEW_URL, "oeId=2000612", "de/thw/map/thw-ov-aachen.html");
        EmbeddedHttpServer.setContent(MAPVIEW_URL, "oeId=2000121", "de/thw/map/thw-ov-aalen.html");
        EmbeddedHttpServer.setContent(MAPVIEW_URL, "oeId=2000108", "de/thw/map/thw-ov-achern.html");
        EmbeddedHttpServer.setContent(MAPVIEW_URL, "oeId=2000429", "de/thw/map/thw-ov-achim.html");
        EmbeddedHttpServer.setContent(MAPVIEW_URL, "oeId=2000041", "de/thw/map/thw-ov-backnang.html");
    }

    @AfterClass
    public static void tearDownServer() throws Exception {
        EmbeddedHttpServer.stop();
    }

    @Before
    public void setUp() throws Exception {
        ThwCrawlerConfiguration thwCrawlerConfiguration = new ThwCrawlerConfiguration();
        thwCrawlerConfiguration.setDomain("http://localhost:" + EmbeddedHttpServer.PORT + "/");
        thwCrawlerConfiguration.setFollowDomainNames(false);
        thwCrawlerConfiguration.setResultsPerPage(2);
        thwCrawlerConfiguration.setHttpRequestTimeout(3000);
        thwCrawlerOrganisationReader = new ThwCrawlerOrganisationReader(thwCrawlerConfiguration, pictureRepository, indexManager);
    }

    private static String getOrganisationUrl(final String letter, final String name) {
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
    public void organisationCanBeRead() throws Exception {
        Organisation organisation = thwCrawlerOrganisationReader.read();
        assertThat(organisation.getAddresses())
            .isNotNull()
            .hasSize(1);
        assertThat(organisation.getAddresses().get(0).getStreet())
            .isEqualTo("Eckenerstraße 52");
    }

    @Test
    public void organisationCanBeRead_withCoordinatesOfOrganisation_returnsOrganisationWithCoordinates() throws Exception {
        // Arrange


        // Act
        Organisation organisation = thwCrawlerOrganisationReader.read();

        // Assert
        assertThat(organisation).isNotNull();
        assertThat(organisation.getName()).isEqualTo("THW Ortsverband Aachen");
        List<Address> addresses = organisation.getAddresses();
        assertThat(addresses)
            .isNotNull()
            .hasSize(1);
        GeoPoint location = addresses.get(0).getLocation();
        assertThat(location).isNotNull();
        assertThat(location.getLat()).isEqualTo(50.756488, Offset.offset(0.0000001));
        assertThat(location.getLon()).isEqualTo(6.158488, Offset.offset(0.0000001));
    }

    @Test
    public void read_executeTwice_verifyOrganisationCorrect() throws Exception {
        // Act
        Organisation organisation1 = thwCrawlerOrganisationReader.read();
        Organisation organisation2 = thwCrawlerOrganisationReader.read();

        // Assert
        assertThat(organisation1).isNotNull();
        assertThat(organisation2).isNotNull();
        assertThat(organisation1.getName()).isEqualTo("THW Ortsverband Aachen");
        assertThat(organisation2.getName()).isEqualTo("THW Ortsverband Aalen");
    }

    @Test
    public void lastReadOvIsBacknang() throws Exception {
        Organisation nextOrganisation = null;
        Organisation organisation;
        do {
            organisation = nextOrganisation;
            nextOrganisation = thwCrawlerOrganisationReader.read();
        } while (nextOrganisation != null);

        assertThat(organisation).isNotNull();
        assertThat(organisation.getName()).isEqualTo("THW Ortsverband Backnang");
    }

    @Test
    public void read_withGroupsOfAalen_returnsCorrectListOfOrganisations() throws Exception {
        // Arrange
        // Start with the second one, less complexity in group structure
        thwCrawlerOrganisationReader.read();

        // Act
        Organisation organisation = thwCrawlerOrganisationReader.read();

        // Assert
        assertThat(organisation).isNotNull();
        assertThat(organisation.getName()).isEqualTo("THW Ortsverband Aalen");
        List<Group> groups = organisation.getGroups();
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
    public void duplicateGroupsAreNotImported() throws Exception {
        // e.g. zugtrupp is only imported once even if there is a zugtrupp for each TZ

        // Arrange

        // Act
        Organisation organisation = thwCrawlerOrganisationReader.read();

        // Assert
        assertThat(organisation.getGroups())
            .isNotNull()
            .hasSize(9);
    }

}
