package de.helfenkannjeder.helfomat.thw.crawler;

import de.helfenkannjeder.helfomat.EmbeddedHttpServer;
import de.helfenkannjeder.helfomat.domain.Group;
import de.helfenkannjeder.helfomat.domain.Organisation;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SpringBootTest
public class ThwCrawlerItemReaderTest {

    private static final String OVERVIEW_URL = "/DE/THW/Bundesanstalt/Dienststellen/dienststellen_node.html";
    private ThwCrawlerItemReader thwCrawlerItemReader;

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
    }

    @Before
    public void setUp() throws Exception {
        thwCrawlerItemReader = new ThwCrawlerItemReader("http://localhost:" + EmbeddedHttpServer.PORT + "/", 2, 3000);
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
    public void read_withGroupsOfAachen_returnsCorrectListOfOrganisations() throws Exception {
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
}