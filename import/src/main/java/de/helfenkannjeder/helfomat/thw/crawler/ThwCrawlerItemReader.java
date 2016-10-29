package de.helfenkannjeder.helfomat.thw.crawler;

import com.google.common.base.Preconditions;
import de.helfenkannjeder.helfomat.configuration.HelfomatConfiguration;
import de.helfenkannjeder.helfomat.domain.Address;
import de.helfenkannjeder.helfomat.domain.GeoPoint;
import de.helfenkannjeder.helfomat.domain.Group;
import de.helfenkannjeder.helfomat.domain.Organisation;
import de.helfenkannjeder.helfomat.domain.Question;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
@JobScope
public class ThwCrawlerItemReader implements ItemReader<Organisation> {

    private static final Logger LOGGER = Logger.getLogger(ThwCrawlerItemReader.class);

    private HelfomatConfiguration helfomatConfiguration;

    private Iterator<Element> iterator;
	private boolean followDomainNames;
	private int resultsPerPage;
	private int httpRequestTimeout;
	private char currentLetter = 'A';
	private int currentPage = 1;
	private String domain;
    private static final Pattern LATITUDE_PATTERN = Pattern.compile("lat = parseFloat\\((\\d+\\.\\d+)\\)");
    private static final Pattern LONGITUDE_PATTERN = Pattern.compile("lng = parseFloat\\((\\d+\\.\\d+)\\)");
	private String mapPin;
	private String logo;

	@Autowired
    public ThwCrawlerItemReader(HelfomatConfiguration helfomatConfiguration,
								@Value("${crawler.thw.domain}") String domain,
								@Value("${crawler.thw.followDomainNames:true}") boolean followDomainNames,
								@Value("${crawler.thw.resultsPerPage}") int resultsPerPage,
								@Value("${crawler.thw.httpRequestTimeout}") int httpRequestTimeout,
								@Value("${crawler.thw.mapPin}") String mapPin,
								@Value("${crawler.thw.logo}") String logo) {
        this.helfomatConfiguration = helfomatConfiguration;
        this.domain = domain;
		this.followDomainNames = followDomainNames;
		this.resultsPerPage = resultsPerPage;
		this.httpRequestTimeout = httpRequestTimeout;
		this.mapPin = mapPin;
		this.logo = logo;
	}

	@Override
	public Organisation read() throws Exception {
		if (iterator == null || !iterator.hasNext()) {
			requestOverviewPage(currentLetter, currentPage++);
			if (!iterator.hasNext() && currentLetter <= 'Z') {
				currentLetter++;
				currentPage = 1;
				LOGGER.debug("Next letter: " + currentLetter);
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
		LOGGER.debug("requested document: " + document.location());
		Elements oeLinks = document.select("[href*=SharedDocs/Organisationseinheiten/DE/Ortsverbaende]");
		iterator = oeLinks.iterator();
	}

	private Organisation extractOrganisation(Document oeDetailsDocument) throws IOException {
		Organisation organisation = new Organisation();
		organisation.setId(UUID.randomUUID().toString());

        String organisationName = "THW " + oeDetailsDocument.select("div#main").select(".photogallery").select(".isFirstInSlot").text();
        LOGGER.info("Read organisation: " + organisationName);
        organisation.setName(Preconditions.checkNotNull(organisationName));

		Elements contactDataDiv = oeDetailsDocument.select(".contact-data");
		organisation.setWebsite(contactDataDiv.select(".url").select("a").attr("href"));

		organisation.setMapPin(mapPin);
		organisation.setLogo(logo);

		Address address = extractAddressFromDocument(oeDetailsDocument);
		organisation.setAddresses(Collections.singletonList(address));

        List<Group> groups = extractGroups(oeDetailsDocument);
        organisation.setGroups(groups);

        organisation.setQuestions(extractQuestions(groups));

		LOGGER.trace("New organisation: " + organisation);
		return organisation;
	}

    private List<Question> extractQuestions(List<Group> groups) {
        return helfomatConfiguration.getQuestions().stream()
                .map(questionMapping -> {
                    Question question = new Question(
                            questionMapping.getUid(),
                            questionMapping.getQuestion(),
                            questionMapping.getPosition(),
                            questionMapping.getDefaultAnswer()
                    );

                    for (HelfomatConfiguration.QuestionMapping.GroupMapping group : questionMapping.getGroups()) {
                        if (groupExistsWithPhrase(groups, group.getPhrase())) {
                            question.setAnswer(group.getAnswer());
                        }
                    }

                    return question;
                })
                .collect(Collectors.toList());
    }

    private boolean groupExistsWithPhrase(List<Group> groups, String phrase) {
        return groups.stream().filter(group -> group.getName().contains(phrase)).findFirst().isPresent();
    }

    private List<Group> extractGroups(Document oeDetailsDocument) {
		List<Group> groups = new ArrayList<>();
		Elements technicalUnits = oeDetailsDocument.select("ul#accordion-box").select("h4");
		for (Element technicalUnit : technicalUnits) {
			Elements links = technicalUnit.select("a");
			Element nameElement = technicalUnit;
			if (links.size() == 1) {
				nameElement = links.first();
			}
			Group group = new Group();
			group.setName(nameElement.text());
			groups.add(group);
		}
		return groups;
	}

	private Address extractAddressFromDocument(Document oeDetailsDocument) throws IOException {
		Address address = new Address();

		Elements contactDataDiv = oeDetailsDocument.select(".contact-data");
		Elements addressDiv = contactDataDiv.select(".adr");
		address.setZipcode(addressDiv.select(".postal-code").text());
		address.setCity(addressDiv.select(".locality").text());
		address.setStreet(addressDiv.select(".street-address").text());

		address.setLocation(extractLocationFromDocument(oeDetailsDocument));

		return address;
	}

	private GeoPoint extractLocationFromDocument(Document oeDetailsDocument) throws IOException {
		String mapLink = oeDetailsDocument.select("a#servicemaplink").attr("href");

		if (!followDomainNames) {
            URL url = new URL(mapLink);
            URL domain = new URL(this.domain);
            URL resultUrl = new URL(domain.getProtocol(), domain.getHost(), domain.getPort(), url.getFile());
            mapLink = resultUrl.toExternalForm();
        }

		Document document = Jsoup.connect(mapLink)
				.timeout(httpRequestTimeout)
				.get();
        LOGGER.debug("Requested document: " + document.location());
        String javascriptContent = document.select("script[type=text/javascript]:not(script[src])").html();

        double latitude = extractCoordinateFromJavascript(javascriptContent, LATITUDE_PATTERN);
        double longitude = extractCoordinateFromJavascript(javascriptContent, LONGITUDE_PATTERN);
        return new GeoPoint(latitude, longitude);
	}

    private double extractCoordinateFromJavascript(String javascriptContent, Pattern pattern) {
        Matcher matcher = pattern.matcher(javascriptContent);
        if (matcher.find()) {
            return Double.parseDouble(matcher.group(1));
        }
        throw new ParseException("Cannot find coordinate inside of javascript, used " + pattern.toString());
    }
}
