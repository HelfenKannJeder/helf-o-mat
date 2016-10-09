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
import org.springframework.stereotype.Component;

@Component
public class ThwCrawlerItemReader implements ItemReader<Organisation> {

	private Iterator<Element> iterator;
	private int resultsPerPage;
	private char currentLetter = 'A';
	private int currentPage = 1;
	private String domain;

	public ThwCrawlerItemReader(@Value("${crawler.thw.domain}") String domain,
								@Value("${crawler.thw.resultsPerPage}") int resultsPerPage) {
		this.domain = domain;
		this.resultsPerPage = resultsPerPage;
	}

	@Override
	public Organisation read() throws Exception {
		if (iterator == null || !iterator.hasNext()) {
			requestOverviewPage(currentLetter, currentPage++);
			if (!iterator.hasNext() && currentLetter <= 'Z') {
				currentLetter++;
				currentPage = 1;
				System.out.println("next Letter: " + currentLetter);
				requestOverviewPage(currentLetter, currentPage);
			}
		}

		if (!iterator.hasNext()) {
			return null;
		}

		return readNextOrganisationItem();
	}

	private Organisation readNextOrganisationItem() throws IOException {
		try {
			Element oeLink = iterator.next();
			String url = domain + oeLink.attr("href");
			Document oeDetailsDocument = Jsoup.connect(url).get();
			return extractOrganisation(oeDetailsDocument);
		}
		catch (Exception ex) {
			return  readNextOrganisationItem();
		}
	}

	private void requestOverviewPage(char letter, int page) throws IOException {
		Elements oeLinks = Jsoup.connect(domain + "DE/THW/Bundesanstalt/Dienststellen/dienststellen_node.html")
				.data("oe_plzort", "PLZ+oder+Ort")
				.data("sorting", "cityasc")
				.data("resultsPerPage", String.valueOf(resultsPerPage))
				.data("oe_typ", "ortsverbaende")
				.data("oe_umkreis", "25") // ignored
				.data("letter", String.valueOf(letter))
				.data("page", String.valueOf(page))
				.get()
				.select("[href*=SharedDocs/Organisationseinheiten/DE/Ortsverbaende]");
		iterator = oeLinks.iterator();
	}

	private Organisation extractOrganisation(Document oeDetailsDocument) {
		Organisation organisation = new Organisation();

		organisation.setName("THW " + oeDetailsDocument.select("div#main").select(".photogallery").select(".isFirstInSlot").text());

		Elements contactDataDiv = oeDetailsDocument.select(".contact-data");
		organisation.setWebsite(contactDataDiv.select(".url").select("a").attr("href"));

		Address address = extractAddressFromDocument(oeDetailsDocument);
		organisation.setAddresses(Collections.singletonList(address));

		return organisation;
	}

	private Address extractAddressFromDocument(Document oeDetailsDocument) {
		Address address = new Address();

		Elements contactDataDiv = oeDetailsDocument.select(".contact-data");
		Elements addressDiv = contactDataDiv.select(".adr");
		address.setZipcode(addressDiv.select(".postal-code").text());
		address.setCity(addressDiv.select(".locality").text());
		address.setStreet(addressDiv.select(".street-address").text());

		return address;
	}
}
