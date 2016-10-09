package de.helfenkannjeder.helfomat.thw.crawler;

import de.helfenkannjeder.helfomat.EmbeddedHttpServer;
import de.helfenkannjeder.helfomat.domain.Organisation;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;

@SpringBootTest
public class ThwDeCrawlerItemReaderTest {

	private ThwDeCrawlerItemReader thwDeCrawlerItemReader;

	@Before
	public void setUp() throws Exception {
		EmbeddedHttpServer.start();
		EmbeddedHttpServer.setContent("/DE/THW/Bundesanstalt/Dienststellen/dienststellen_node.html", "de/thw/overview/a_1.html");
		EmbeddedHttpServer.setContent("/SharedDocs/Organisationseinheiten/DE/Ortsverbaende/A/Aachen_Ortsverband.html", "de/thw/detail/thw-ov-karlsruhe.html");
		thwDeCrawlerItemReader = new ThwDeCrawlerItemReader("http://localhost:" + EmbeddedHttpServer.PORT + "/", 3);
	}

	@Test
	public void organisationCanBeRead() throws Exception {
		Organisation organisation = thwDeCrawlerItemReader.read();
		assertEquals(1, organisation.getAddresses().size());
		assertEquals("Grünhutstraße 9", organisation.getAddresses().get(0).getStreet());
	}

}