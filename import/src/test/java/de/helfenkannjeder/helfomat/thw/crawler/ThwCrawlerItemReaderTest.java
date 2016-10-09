package de.helfenkannjeder.helfomat.thw.crawler;

import de.helfenkannjeder.helfomat.EmbeddedHttpServer;
import org.junit.Before;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ThwCrawlerItemReaderTest {

	private ThwCrawlerItemReader thwCrawlerItemReader;

	@Before
	public void setUp() throws Exception {
		EmbeddedHttpServer.start();
		EmbeddedHttpServer.setContent("/DE/THW/Bundesanstalt/Dienststellen/dienststellen_node.html?oe_plzort=PLZ+oder+Ort&sorting=cityasc&resultsPerPage=2&oe_typ=ortsverbaende&oe_umkreis=25&letter=A&page=1", "de/thw/overview/a_1.html");
		EmbeddedHttpServer.setContent("/DE/THW/Bundesanstalt/Dienststellen/dienststellen_node.html?oe_plzort=PLZ+oder+Ort&sorting=cityasc&resultsPerPage=2&oe_typ=ortsverbaende&oe_umkreis=25&letter=A&page=2", "de/thw/overview/a_2.html");
		EmbeddedHttpServer.setContent("/DE/THW/Bundesanstalt/Dienststellen/dienststellen_node.html?oe_plzort=PLZ+oder+Ort&sorting=cityasc&resultsPerPage=2&oe_typ=ortsverbaende&oe_umkreis=25&letter=A&page=3", "de/thw/overview/a_3.html");
		EmbeddedHttpServer.setContent("/DE/THW/Bundesanstalt/Dienststellen/dienststellen_node.html?oe_plzort=PLZ+oder+Ort&sorting=cityasc&resultsPerPage=2&oe_typ=ortsverbaende&oe_umkreis=25&letter=B&page=1", "de/thw/overview/b_1.html");
		EmbeddedHttpServer.setContent("/SharedDocs/Organisationseinheiten/DE/Ortsverbaende/A/Aachen_Ortsverband.html", "de/thw/detail/thw-ov-aachen.html");
		thwCrawlerItemReader = new ThwCrawlerItemReader("http://localhost:" + EmbeddedHttpServer.PORT + "/", 2, 3000);
	}

//	@Test
//	public void organisationCanBeRead() throws Exception {
//		Organisation organisation = thwCrawlerItemReader.read();
//		assertEquals(1, organisation.getAddresses().size());
//		assertEquals("Eckenerstraße 52", organisation.getAddresses().get(0).getStreet());
//	}
//
//	@Test
//	public void lastReadOvIsBacknang() throws Exception {
//		Organisation organisation = null;
//		while (true) {
//			Organisation nextOrganisation = thwCrawlerItemReader.read();
//			if(nextOrganisation!= null) {
//				organisation = nextOrganisation;
//			}
//			else {
//				break;
//			}
//		}
//
//		assertEquals("THW Ortsverband Backnang", organisation.getName());
//	}
}