package de.helfenkannjeder.helfomat.thw.crawler;

import de.helfenkannjeder.helfomat.domain.Address;
import de.helfenkannjeder.helfomat.domain.Organisation;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.UUID;

@Component
@JobScope
public class ThwCrawlerItemReader implements ItemReader<Organisation> {

    private static final Logger LOGGER = Logger.getLogger(ThwCrawlerItemReader.class);

	private Iterator<Element> iterator;
	private int resultsPerPage;
	private int httpRequestTimeout;
	private char currentLetter = 'A';
	private int currentPage = 1;
	private String domain;

	public ThwCrawlerItemReader(@Value("${crawler.thw.domain}") String domain,
								@Value("${crawler.thw.resultsPerPage}") int resultsPerPage,
								@Value("${crawler.thw.httpRequestTimeout}") int httpRequestTimeout) {
		this.domain = domain;
		this.resultsPerPage = resultsPerPage;
		this.httpRequestTimeout = httpRequestTimeout;
	}

	@Override
	public Organisation read() throws Exception {
		if (iterator == null || !iterator.hasNext()) {
			requestOverviewPage(currentLetter, currentPage++);
			if (!iterator.hasNext() && currentLetter <= 'Z') {
				currentLetter++;
				currentPage = 1;
				LOGGER.debug("next Letter: " + currentLetter);
				requestOverviewPage(currentLetter, currentPage++);
			}
		}

		if (!iterator.hasNext()) {
			return null;
		}

		return readNextOrganisationItem();
	}

	private Organisation readNextOrganisationItem() throws IOException {
			Element oeLink = iterator.next();
			String url = domain + oeLink.attr("href");
			Document oeDetailsDocument = Jsoup.connect(url).timeout(httpRequestTimeout).get();
			return extractOrganisation(oeDetailsDocument);
	}

	private void requestOverviewPage(char letter, int page) throws IOException {
		Document document = Jsoup.connect(domain + "DE/THW/Bundesanstalt/Dienststellen/dienststellen_node.html")
				.timeout(httpRequestTimeout)
				.data("oe_plzort", "PLZ+oder+Ort")
				.data("sorting", "cityasc")
				.data("resultsPerPage", String.valueOf(resultsPerPage))
				.data("oe_typ", "ortsverbaende")
				.data("oe_umkreis", "25") // ignored
				.data("letter", String.valueOf(letter))
				.data("page", String.valueOf(page))
				.get();
		LOGGER.info("requested document: " + document.location());
		Elements oeLinks = document.select("[href*=SharedDocs/Organisationseinheiten/DE/Ortsverbaende]");
		iterator = oeLinks.iterator();
	}

	private Organisation extractOrganisation(Document oeDetailsDocument) {
		Organisation organisation = new Organisation();
		organisation.setId(UUID.randomUUID().toString());

		organisation.setName("THW " + oeDetailsDocument.select("div#main").select(".photogallery").select(".isFirstInSlot").text());

		Elements contactDataDiv = oeDetailsDocument.select(".contact-data");
		organisation.setWebsite(contactDataDiv.select(".url").select("a").attr("href"));

		Address address = extractAddressFromDocument(oeDetailsDocument);
		organisation.setAddresses(Collections.singletonList(address));

		LOGGER.debug("New organisation: " + organisation);
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
