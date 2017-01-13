package de.helfenkannjeder.helfomat.thw.crawler;

import de.helfenkannjeder.helfomat.EmbeddedHttpServer;
import de.helfenkannjeder.helfomat.configuration.HelfomatConfiguration;
import de.helfenkannjeder.helfomat.domain.Address;
import de.helfenkannjeder.helfomat.domain.Group;
import de.helfenkannjeder.helfomat.domain.Organisation;
import de.helfenkannjeder.helfomat.domain.Question;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ThwCrawlerItemReaderTest {

    private static final String OVERVIEW_URL = "/DE/THW/Bundesanstalt/Dienststellen/dienststellen_node.html";
    private static final String MAPVIEW_URL = "/DE/THW/Bundesanstalt/Dienststellen/Kartenansicht/kartenansicht_node.html";
    private ThwCrawlerItemReader thwCrawlerItemReader;

    @Autowired
    private HelfomatConfiguration helfomatConfiguration;

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

    @Before
    public void setUp() throws Exception {
        ThwCrawlerConfiguration thwCrawlerConfiguration = new ThwCrawlerConfiguration();
        thwCrawlerConfiguration.setDomain("http://localhost:" + EmbeddedHttpServer.PORT + "/");
        thwCrawlerConfiguration.setFollowDomainNames(false);
        thwCrawlerConfiguration.setResultsPerPage(2);
        thwCrawlerConfiguration.setHttpRequestTimeout(3000);
        thwCrawlerItemReader = new ThwCrawlerItemReader(thwCrawlerConfiguration);
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
        Organisation organisation = thwCrawlerItemReader.read();
        assertEquals(1, organisation.getAddresses().size());
        assertEquals("Eckenerstraße 52", organisation.getAddresses().get(0).getStreet());
    }

    @Test
    public void organisationCanBeRead_withCoordinatesOfOrganisation_returnsOrganisationWithCoordinates() throws Exception {
        // Arrange


        // Act
        Organisation organisation = thwCrawlerItemReader.read();

        // Assert
        assertEquals("THW Ortsverband Aachen", organisation.getName());
        List<Address> addresses = organisation.getAddresses();
        assertNotNull(addresses);
        assertEquals(1, addresses.size());
        assertNotNull(addresses.get(0).getLocation());
        assertEquals(50.756488, addresses.get(0).getLocation().getLat(), 0.0000001);
        assertEquals(6.158488, addresses.get(0).getLocation().getLon(), 0.0000001);
    }

    @Test
    public void read_executeTwice_verifyOrganisationCorrect() throws Exception {
        // Act
        Organisation organisation1 = thwCrawlerItemReader.read();
        Organisation organisation2 = thwCrawlerItemReader.read();

        // Assert
        assertNotNull(organisation1);
        assertNotNull(organisation2);
        assertEquals("THW Ortsverband Aachen", organisation1.getName());
        assertEquals("THW Ortsverband Aalen", organisation2.getName());
    }

    @Test
    public void lastReadOvIsBacknang() throws Exception {
        Organisation nextOrganisation = null;
        Organisation organisation;
        do {
            organisation = nextOrganisation;
            nextOrganisation = thwCrawlerItemReader.read();
        } while (nextOrganisation != null);

        assertNotNull(organisation);
        assertEquals("THW Ortsverband Backnang", organisation.getName());
    }

    @Test
    public void read_withGroupsOfAalen_returnsCorrectListOfOrganisations() throws Exception {
        // Arrange
        // Start with the second one, less complexity in group structure
        thwCrawlerItemReader.read();

        // Act
        Organisation organisation = thwCrawlerItemReader.read();

        // Assert
        assertNotNull(organisation);
        assertEquals("THW Ortsverband Aalen", organisation.getName());
        List<Group> groups = organisation.getGroups();
        assertNotNull(groups);
        assertEquals(5, groups.size());
        assertEquals("Zugtrupp", groups.get(0).getName());
        assertEquals("Bergungsgruppe 1", groups.get(1).getName());
        assertEquals("Bergungsgruppe 2, Typ B", groups.get(2).getName());
        assertEquals("Fachgruppe Räumen Typ A (ALT)", groups.get(3).getName());
        assertEquals("Fachgruppe Ortung Typ B", groups.get(4).getName());
    }

    @Test
    public void duplicateGroupsAreNotImported() throws Exception {
        // e.g. zugtrupp is only imported once even if there is a zugtrupp for each TZ

        // Arrange

        // Act
        Organisation organisation = thwCrawlerItemReader.read();

        // Assert
        assertEquals(9, organisation.getGroups().size());
    }

    private static void assertQuestion(String expectedQestion, Question.Answer expectedAnswer, Question question) {
        assertNotNull(question);
        assertEquals(expectedQestion, question.getQuestion());
        assertEquals(expectedAnswer, question.getAnswer());
    }

}