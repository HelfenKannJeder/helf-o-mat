package de.helfenkannjeder.helfomat.infrastructure.thwde;

import de.helfenkannjeder.helfomat.core.IndexManager;
import de.helfenkannjeder.helfomat.core.ProfileRegistry;
import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint;
import de.helfenkannjeder.helfomat.core.organisation.Address;
import de.helfenkannjeder.helfomat.core.organisation.Group;
import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationReader;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationType;
import de.helfenkannjeder.helfomat.core.organisation.PictureId;
import de.helfenkannjeder.helfomat.core.picture.DownloadFailedException;
import de.helfenkannjeder.helfomat.core.picture.PictureRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Collections.singletonList;

@Component
@JobScope
@Order(200)
@Profile("!" + ProfileRegistry.DISABLE_THWDE_IMPORT)
public class ThwCrawlerOrganisationReader implements ItemReader<Organisation>, OrganisationReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThwCrawlerOrganisationReader.class);

    private final ThwCrawlerConfiguration thwCrawlerConfiguration;
    private final PictureRepository pictureRepository;
    private final IndexManager indexManager;

    private Iterator<Element> iterator;

    private char currentLetter = 'A';
    private int currentPage = 1;
    private static final Pattern LATITUDE_PATTERN = Pattern.compile("lat = parseFloat\\((\\d+\\.\\d+)\\)");
    private static final Pattern LONGITUDE_PATTERN = Pattern.compile("lng = parseFloat\\((\\d+\\.\\d+)\\)");

    @Autowired
    public ThwCrawlerOrganisationReader(ThwCrawlerConfiguration thwCrawlerConfiguration, PictureRepository pictureRepository, IndexManager
        indexManager) {
        this.thwCrawlerConfiguration = thwCrawlerConfiguration;
        this.pictureRepository = pictureRepository;
        this.indexManager = indexManager;
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

        Organisation organisation = readNextOrganisationItem();
        LOGGER.debug("Got organisation '" + organisation.getName() + "' from thw.de website.");
        return organisation;
    }

    private Organisation readNextOrganisationItem() throws IOException {
        Element oeLink = iterator.next();
        String url = this.thwCrawlerConfiguration.getDomain() + oeLink.attr("href");
        Document oeDetailsDocument = Jsoup.connect(url).timeout(this.thwCrawlerConfiguration.getHttpRequestTimeout()).get();
        return extractOrganisation(oeDetailsDocument);
    }

    private void requestOverviewPage(char letter, int page) throws IOException {
        Document document = Jsoup.connect(this.thwCrawlerConfiguration.getDomain() + "DE/THW/Bundesanstalt/Dienststellen/dienststellen_node.html")
            .timeout(this.thwCrawlerConfiguration.getHttpRequestTimeout())
            .data("oe_plzort", "PLZ+oder+Ort")
            .data("sorting", "cityasc")
            .data("resultsPerPage", String.valueOf(this.thwCrawlerConfiguration.getResultsPerPage()))
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
        String organisationName = "THW " + oeDetailsDocument.select("div#main").select(".photogallery").select(".isFirstInSlot").text();
        LOGGER.info("Read organisation: " + organisationName);

        Elements contactDataDiv = oeDetailsDocument.select(".contact-data");
        List<Group> groups = extractDistinctGroups(oeDetailsDocument);

        Organisation organisation = new Organisation.Builder()
            .setId(UUID.randomUUID().toString())
            .setType(OrganisationType.THW.toString())
            .setName(checkNotNull(organisationName))
            .setWebsite(contactDataDiv.select(".url").select("a").attr("href"))
            .setMapPin(this.thwCrawlerConfiguration.getMapPin())
            .setLogo(toPicture(this.thwCrawlerConfiguration.getLogo()))
            .setAddresses(singletonList(extractAddressFromDocument(oeDetailsDocument)))
            .setGroups(groups)
            .build();

        LOGGER.trace("New organisation: " + organisation);
        return organisation;
    }

    private PictureId toPicture(String picture) throws IOException {
        try {
            return this.pictureRepository.savePicture(picture, this.indexManager.getCurrentIndex(), new PictureId());
        } catch (DownloadFailedException e) {
            LOGGER.warn("Failed to download picture", e);
            return null;
        }
    }

    private List<Group> extractDistinctGroups(Document oeDetailsDocument) {
        List<Group> groups = new ArrayList<>();
        Elements groupElements = oeDetailsDocument.select("ul#accordion-box").select("h4");
        for (Element groupElement : groupElements) {
            Group group = new Group();
            group.setName(getGroupName(groupElement));
            groups.add(group);
        }
        return groups
            .stream()
            .distinct()
            .collect(Collectors.toList());
    }

    private String getGroupName(Element headlineElement) {
        Elements linkElement = headlineElement.select("a");
        if (!linkElement.isEmpty()) { // <h4><a>...</a></h4>
            return linkElement.first().text();
        }
        // <h4>...</h4>
        return headlineElement.text();
    }

    private Address extractAddressFromDocument(Document oeDetailsDocument) throws IOException {
        Address address = new Address.Builder().build();

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

        if (!this.thwCrawlerConfiguration.isFollowDomainNames()) {
            URL url = new URL(mapLink);
            URL domain = new URL(this.thwCrawlerConfiguration.getDomain());
            URL resultUrl = new URL(domain.getProtocol(), domain.getHost(), domain.getPort(), url.getFile());
            mapLink = resultUrl.toExternalForm();
        }

        Document document = Jsoup.connect(mapLink)
            .timeout(this.thwCrawlerConfiguration.getHttpRequestTimeout())
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