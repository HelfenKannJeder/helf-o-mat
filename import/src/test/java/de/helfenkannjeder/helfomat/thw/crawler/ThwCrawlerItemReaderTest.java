package de.helfenkannjeder.helfomat.thw.crawler;

import de.helfenkannjeder.helfomat.EmbeddedHttpServer;
import de.helfenkannjeder.helfomat.domain.Organisation;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;

@SpringBootTest
public class ThwCrawlerItemReaderTest {

    private ThwCrawlerItemReader thwCrawlerItemReader;

    @Before
    public void setUp() throws Exception {
        EmbeddedHttpServer.start();
        EmbeddedHttpServer.setContent(getOverviewUrl("A", 1), "de/thw/overview/a_1.html");
        EmbeddedHttpServer.setContent(getOverviewUrl("A", 2), "de/thw/overview/a_2.html");
        EmbeddedHttpServer.setContent(getOverviewUrl("A", 3), "de/thw/overview/a_3.html");
        EmbeddedHttpServer.setContent(getOverviewUrl("B", 1), "de/thw/overview/b_1.html");
        EmbeddedHttpServer.setContent(getOrganisationUrl("A", "Aachen_Ortsverband"), "de/thw/detail/thw-ov-aachen.html");

        thwCrawlerItemReader = new ThwCrawlerItemReader("http://localhost:" + EmbeddedHttpServer.PORT + "/", 2, 3000);
    }

    private String getOrganisationUrl(final String letter, final String name) {
        return "/SharedDocs/Organisationseinheiten/DE/Ortsverbaende/" + letter + "/" + name + ".html";
    }

    private String getOverviewUrl(final String letter, final int page) {
        return "/DE/THW/Bundesanstalt/Dienststellen/dienststellen_node.html?" +
                "oe_plzort=PLZ+oder+Ort&" +
                "sorting=cityasc&" +
                "resultsPerPage=2&" +
                "oe_typ=ortsverbaende&" +
                "oe_umkreis=25&" +
                "letter=" + letter + "&" +
                "page=" + page;
    }

    @Test
    @Ignore
    public void organisationCanBeRead() throws Exception {
        Organisation organisation = thwCrawlerItemReader.read();
        assertEquals(1, organisation.getAddresses().size());
        assertEquals("Eckenerstraße 52", organisation.getAddresses().get(0).getStreet());
    }

    @Test
    @Ignore
    public void lastReadOvIsBacknang() throws Exception {
        Organisation organisation = null;
        while (true) {
            Organisation nextOrganisation = thwCrawlerItemReader.read();
            if (nextOrganisation != null) {
                organisation = nextOrganisation;
            } else {
                break;
            }
        }

        assertEquals("THW Ortsverband Backnang", organisation.getName());
    }
}