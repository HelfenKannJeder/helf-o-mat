package de.helfenkannjeder.helfomat.infrastructure.thwde

import com.google.common.base.Preconditions
import de.helfenkannjeder.helfomat.core.ProfileRegistry
import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint
import de.helfenkannjeder.helfomat.core.organization.*
import de.helfenkannjeder.helfomat.core.picture.DownloadFailedException
import de.helfenkannjeder.helfomat.core.picture.PictureId
import de.helfenkannjeder.helfomat.core.picture.PictureStorageService
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import org.slf4j.LoggerFactory
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ParseException
import org.springframework.context.annotation.Profile
import org.springframework.core.annotation.Order
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import org.springframework.util.StreamUtils
import java.io.IOException
import java.net.URL
import java.nio.charset.Charset
import java.util.*
import java.util.regex.Pattern

@Component
@JobScope
@Order(200)
@Profile("!" + ProfileRegistry.DISABLE_THWDE_IMPORT)
open class ThwCrawlerOrganizationReader constructor(
    private val thwCrawlerConfiguration: ThwCrawlerConfiguration,
    private val pictureStorageService: PictureStorageService
) : ItemReader<Organization>, OrganizationReader {
    private val logger = LoggerFactory.getLogger(ThwCrawlerOrganizationReader::class.java)
    private val latitudePattern = Pattern.compile("lat = parseFloat\\((\\d+\\.\\d+)\\)")
    private val longitudePattern = Pattern.compile("lng = parseFloat\\((\\d+\\.\\d+)\\)")

    private var iterator: Iterator<Element>? = null
    private var currentLetter = 'A'
    private var currentPage = 1
    private val logoPictureid = toPictureIdFromClasspathResource("thwde/logo.png")
    private val teaserPictureId = toPictureIdFromClasspathResource("thwde/teaser.jpg")


    override fun getName(): String {
        return "thw-crawler"
    }

    @Throws(Exception::class)
    override fun read(): Organization? {
        if (iterator == null || !iterator!!.hasNext()) {
            requestOverviewPage(currentLetter, currentPage++)
            if (!iterator!!.hasNext() && currentLetter <= 'Z') {
                currentLetter++
                currentPage = 1
                logger.debug("Next letter: $currentLetter")
                requestOverviewPage(currentLetter, currentPage++)
            }
        }
        if (!iterator!!.hasNext()) {
            return null
        }
        val organization = readNextOrganizationItem()
        logger.debug("Got organization '" + organization.name + "' from thw.de website.")
        return organization
    }

    private fun readNextOrganizationItem(): Organization {
        val oeLink = iterator!!.next()
        val url = thwCrawlerConfiguration.domain + oeLink.attr("href")
        val oeDetailsDocument = Jsoup.connect(url).timeout(thwCrawlerConfiguration.httpRequestTimeout).get()
        return extractOrganization(oeDetailsDocument)
    }

    private fun requestOverviewPage(letter: Char, page: Int) {
        val document = Jsoup.connect(thwCrawlerConfiguration.domain + "DE/THW/Bundesanstalt/Dienststellen/dienststellen_node.html")
            .timeout(thwCrawlerConfiguration.httpRequestTimeout)
            .data("oe_plzort", "PLZ+oder+Ort")
            .data("sorting", "cityasc")
            .data("resultsPerPage", thwCrawlerConfiguration.resultsPerPage.toString())
            .data("oe_typ", "ortsverbaende")
            .data("oe_umkreis", "25") // ignored
            .data("letter", letter.toString())
            .data("page", page.toString())
            .get()
        logger.debug("requested document: " + document.location())
        val oeLinks = document.select("[href*=SharedDocs/Organisationseinheiten/DE/Ortsverbaende]")
        iterator = oeLinks.iterator()
    }

    private fun extractOrganization(oeDetailsDocument: Document): Organization {
        val organizationName = "THW " + oeDetailsDocument.select("div#main").select(".photogallery").select(".isFirstInSlot").text()
        logger.info("Read organization: $organizationName")
        val contactDataDiv = oeDetailsDocument.select(".contact-data")
        val groups = extractDistinctGroups(oeDetailsDocument)
        val address = extractAddressFromDocument(oeDetailsDocument)
        val organization = Organization.Builder()
            .setId(OrganizationId())
            .setOrganizationType(OrganizationType.THW)
            .setName(Preconditions.checkNotNull(organizationName))
            .setPictures(listOf(teaserPictureId))
            .setWebsite(contactDataDiv.select(".url").select("a").attr("href"))
            .setMapPin(thwCrawlerConfiguration.mapPin)
            .setLogo(logoPictureid)
            .setAddresses(listOf(address))
            .setDefaultAddress(address)
            .setGroups(groups)
            .setContactPersons(listOf(toContactPerson(oeDetailsDocument, contactDataDiv)))
            .build()
        logger.trace("New organization: $organization")
        return organization
    }

    private fun toContactPerson(oeDetailsDocument: Document, contactDataDiv: Elements): ContactPerson {
        var pictureId: PictureId? = null
        val imageSrc = oeDetailsDocument
            .select(".personenBox")
            .select(".vcard")
            .select("img")
            .attr("src")
        if (!imageSrc.contains("NoElementPerson.jpg") && "" != imageSrc) {
            pictureId = toPicture(thwCrawlerConfiguration.domain + imageSrc)
        }
        return ContactPerson.Builder()
            .setFirstname(contactDataDiv.select(".given-name").text())
            .setLastname(contactDataDiv.select(".family-name").text())
            .setRank(contactDataDiv.select(".title").select("span").text())
            .setTelephone(contactDataDiv.select(".tel").first().select("span.value").text())
            .setPicture(pictureId)
            .build()
    }

    private fun toPicture(picture: String): PictureId? {
        return try {
            val pictureId = toPictureId(picture)
            if (pictureStorageService.existPicture(pictureId)) {
                return pictureId
            }
            pictureStorageService.savePicture(picture, pictureId)
            pictureId
        } catch (e: DownloadFailedException) {
            logger.warn("Failed to download picture", e)
            null
        }
    }

    private fun toPictureIdFromClasspathResource(imagePath: String): PictureId {
        val pictureId = toPictureId("classpath:$imagePath")
        if (pictureStorageService.existPicture(pictureId)) {
            return pictureId
        }
        val imageByteArray = StreamUtils.copyToByteArray(ClassPathResource(imagePath).inputStream)
        pictureStorageService.savePicture(imageByteArray, pictureId)
        return pictureId
    }

    fun toPictureId(url: String): PictureId {
        return PictureId(UUID.nameUUIDFromBytes(url.toByteArray(Charset.defaultCharset())).toString())
    }

    private fun extractDistinctGroups(oeDetailsDocument: Document): List<Group> {
        val groupElements = oeDetailsDocument.select("ul#accordion-box").select("h4")
        return groupElements
            .map { groupElement: Element ->
                Group.Builder()
                    .setName(getGroupName(groupElement))
                    .setDescription(getGroupDescription(groupElement))
                    .build()
            }
            .distinct()
    }

    private fun getGroupName(headlineElement: Element): String {
        val linkElement = headlineElement.select("a")
        return if (!linkElement.isEmpty()) linkElement.first().text()// <h4><a>...</a></h4>
        else headlineElement.text() // <h4>...</h4>
    }

    private fun getGroupDescription(headlineElement: Element): String? {
        var linkElement = headlineElement.select("a")
        if (linkElement.isEmpty()) {
            linkElement = headlineElement.parent().select("a")
        }
        val first = linkElement.first() ?: return null
        val href = first.attr("href")
        val groupPage = thwCrawlerConfiguration.domain + href
        return try {
            val document = Jsoup.connect(groupPage)
                .timeout(thwCrawlerConfiguration.httpRequestTimeout)
                .get()
            document.select(".abstract").select("p").text()
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    private fun extractAddressFromDocument(oeDetailsDocument: Document): Address {
        val contactDataDiv = oeDetailsDocument.select(".contact-data")
        val addressDiv = contactDataDiv.select(".adr")
        return Address.Builder()
            .setZipcode(addressDiv.select(".postal-code").text())
            .setCity(addressDiv.select(".locality").text())
            .setStreet(addressDiv.select(".street-address").text())
            .setLocation(extractLocationFromDocument(oeDetailsDocument))
            .build()
    }

    private fun extractLocationFromDocument(oeDetailsDocument: Document): GeoPoint {
        var mapLink = oeDetailsDocument.select("a#servicemaplink").attr("href")
        if (!thwCrawlerConfiguration.isFollowDomainNames) {
            val url = URL(mapLink)
            val domain = URL(thwCrawlerConfiguration.domain)
            val resultUrl = URL(domain.protocol, domain.host, domain.port, url.file)
            mapLink = resultUrl.toExternalForm()
        }
        val document = Jsoup.connect(mapLink)
            .timeout(thwCrawlerConfiguration.httpRequestTimeout)
            .get()
        logger.debug("Requested document: " + document.location())
        val javascriptContent = document.select("script[type=text/javascript]:not(script[src])").html()
        val latitude = extractCoordinateFromJavascript(javascriptContent, latitudePattern)
        val longitude = extractCoordinateFromJavascript(javascriptContent, longitudePattern)
        return GeoPoint(latitude, longitude)
    }

    private fun extractCoordinateFromJavascript(javascriptContent: String, pattern: Pattern): Double {
        val matcher = pattern.matcher(javascriptContent)
        if (matcher.find()) {
            return matcher.group(1).toDouble()
        }
        throw ParseException("Cannot find coordinate inside of javascript, used $pattern")
    }

}