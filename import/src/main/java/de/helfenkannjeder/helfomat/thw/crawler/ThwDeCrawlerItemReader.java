package de.helfenkannjeder.helfomat.thw.crawler;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;

import de.helfenkannjeder.helfomat.domain.Address;
import de.helfenkannjeder.helfomat.domain.Organisation;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;

public class ThwDeCrawlerItemReader implements ItemReader<Organisation> {

	private Iterator<Element> iterator;
	private int resultsPerPage;
	private char currentLetter = 'A';
	private int currentPage = 0;
	private String domain;

	public ThwDeCrawlerItemReader(@Value("crawler.thw.domain") String domain,
								  @Value("crawler.thw.resultsPerPage") int resultsPerPage) {
		this.domain = domain;
		this.resultsPerPage = resultsPerPage;
	}

	@Override
	public Organisation read() throws Exception {
		if (iterator == null ||!iterator.hasNext()) {
			requestOverviewPage(currentLetter, currentPage++);
			if (!iterator.hasNext() && currentLetter <= 'Z') {
				currentLetter++;
				return read();
			}
		}

		Element oeLink = iterator.next();
		String url = domain + oeLink.attr("href");
		Document oeDetailsDocument = Jsoup.connect(url).get();
		Address address = extractAddressFromDocument(oeDetailsDocument);


		Organisation organisation = new Organisation();
		organisation.setAddresses(Collections.singletonList(address));
		return organisation;
	}

	private void requestOverviewPage(char forLetter, int page) throws IOException {
		Elements oeLinks = Jsoup.connect(domain + "DE/THW/Bundesanstalt/Dienststellen/dienststellen_node.html")
				.data("oe_plzort", "PLZ+oder+Ort")
				.data("sorting", "cityasc")
				.data("resultsPerPage", String.valueOf(resultsPerPage))
				.data("oe_typ", "ortsverbaende")
				.data("oe_umkreis", "25") // ignored
				.data("letter", String.valueOf(forLetter))
				.get()
				.select("[href*=SharedDocs/Organisationseinheiten/DE/Ortsverbaende]");
		iterator = oeLinks.iterator();
	}

	private Address extractAddressFromDocument(Document oeDetailsDocument) {
		Address address = new Address();

		Elements contactDataDiv = oeDetailsDocument.select(".contact-data");
		Elements addressDiv = contactDataDiv.select(".adr");
		address.setZipcode(addressDiv.select(".postal-code").text());
		address.setCity(addressDiv.select(".locality").text());
		address.setStreet(addressDiv.select(".street-address").text());
		address.setWebsite(contactDataDiv.select(".url").select("a").attr("href"));

		return address;
	}
}
