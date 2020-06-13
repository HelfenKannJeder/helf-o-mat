package de.helfenkannjeder.helfomat.infrastructure.typo3

import de.helfenkannjeder.helfomat.api.picture.PictureStorageService
import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint
import de.helfenkannjeder.helfomat.core.organization.*
import de.helfenkannjeder.helfomat.core.picture.DownloadFailedException
import de.helfenkannjeder.helfomat.core.picture.PictureId
import de.helfenkannjeder.helfomat.infrastructure.typo3.domain.*
import org.slf4j.LoggerFactory
import org.springframework.batch.item.ItemProcessor
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.nio.charset.Charset
import java.time.DayOfWeek
import java.time.LocalTime
import java.util.*
import java.util.stream.Collectors
import kotlin.collections.ArrayList

/**
 * @author Valentin Zickner
 */
@Component
@Transactional(propagation = Propagation.REQUIRED, transactionManager = "legacyTransactionManager")
open class Typo3OrganizationProcessor(
    private val pictureStorageService: PictureStorageService
) : ItemProcessor<TOrganization, Organization?> {

    override fun process(tOrganization: TOrganization): Organization? {
        if (organizationIsNoCandidateToImport(tOrganization)) {
            LOGGER.info("Ignore TYPO3 organization '" + tOrganization.name + "'")
            return null
        }
        val defaultAddress = toNullableAddress(tOrganization.defaultaddress)
        val addresses = tOrganization.addresses.map { toAddress(it) }
        return Organization.Builder(
            id = OrganizationId(),
            name = tOrganization.name,
            urlName = tOrganization.name,
            organizationType = OrganizationType.findByName(tOrganization.organizationtype.name)
        )
            .setDescription(tOrganization.description)
            .setLogo(toPicture(tOrganization.logo))
            .setWebsite(UrlUnifier.unifyOrganizationWebsiteUrl(tOrganization.website))
            .setMapPin(unifyOrganizationPins(tOrganization.organizationtype.picture))
            .setPictures(toPictures(extractPictures(tOrganization.pictures)))
            .setContactPersons(toContactPersons(tOrganization.contactPersons).sortedBy { it.firstname })
            .setAddresses(moveDefaultAddressToTheBeginning(addresses, defaultAddress))
            .setDefaultAddress(defaultAddress)
            .setGroups(
                tOrganization.groups.stream().map { tGroup: TGroup -> toGroup(tGroup) }.collect(Collectors.toList())
            )
            .setAttendanceTimes(tOrganization.workinghours.map { toEvent(it) })
            .setVolunteers(tOrganization.employees.filter {
                !(it.motivation?.isEmpty() ?: true)
            }.map { toVolunteer(it) })
            .build()
    }

    private fun moveDefaultAddressToTheBeginning(addresses: List<Address>, defaultAddress: Address?): ArrayList<Address> {
        val newAddresses = ArrayList<Address>(addresses)
        if (defaultAddress != null) {
            if (addresses.contains(defaultAddress)) {
                newAddresses.remove(defaultAddress)
            }
            newAddresses.add(0, defaultAddress)
        }
        return newAddresses
    }

    private fun toNullableAddress(tAddress: TAddress?): Address? = when (tAddress) {
        null -> null
        else -> toAddress(tAddress)
    }

    private fun toVolunteer(tEmployee: TEmployee): Volunteer {
        return Volunteer(
            firstname = tEmployee.prename,
            lastname = tEmployee.surname,
            motivation = tEmployee.motivation ?: "",
            picture = toPicture(tEmployee.pictures)
        )
    }

    private fun toGroup(tGroup: TGroup): Group {
        return Group(
            name = tGroup.name,
            description = tGroup.description,
            contactPersons = toContactPersons(tGroup.contactPersons),
            minimumAge = tGroup.minimumAge,
            maximumAge = tGroup.maximumAge,
            website = tGroup.website
        )
    }

    private fun toEvent(tWorkingHour: TWorkingHour): AttendanceTime {
        return AttendanceTime(
            day = DayOfWeek.of(tWorkingHour.day),
            start = LocalTime.of(tWorkingHour.starttimehour, tWorkingHour.starttimeminute),
            end = LocalTime.of(tWorkingHour.stoptimehour, tWorkingHour.stoptimeminute),
            note = tWorkingHour.addition,
            groups = tWorkingHour.groups.map { toGroup(it) }
        )
    }

    private fun toPictures(pictures: List<String>): List<PictureId> {
        return pictures
            .map { picture: String -> toPicture(picture) }
            .filterNotNull()
    }

    private fun toPicture(picture: String?): PictureId? {
        return if (picture == null || picture == "") {
            null
        } else try {
            val url = "https://helfenkannjeder.de/uploads/pics/$picture"
            LOGGER.info("uploading picture $url")
            val pictureId = toPictureId(url)
            if (pictureStorageService.existPicture(pictureId)) {
                return pictureId
            }
            pictureStorageService.savePicture(
                url,
                pictureId
            )
            pictureId
        } catch (e: DownloadFailedException) {
            LOGGER.warn("Failed to download picture", e)
            null
        }
    }

    private fun toPictureId(url: String): PictureId {
        return PictureId(UUID.nameUUIDFromBytes(url.toByteArray(Charset.defaultCharset())).toString())
    }

    private fun toContactPersons(employees: List<TEmployee>): List<ContactPerson> {
        return employees.stream()
            .map { tEmployee: TEmployee -> toContactPerson(tEmployee) }
            .collect(Collectors.toList())
    }

    private fun toContactPerson(tEmployee: TEmployee): ContactPerson {
        return ContactPerson(
            firstname = tEmployee.prename,
            lastname = tEmployee.surname,
            rank = tEmployee.rank,
            telephone = tEmployee.telephone,
            mail = tEmployee.mail,
            picture = toPicture(tEmployee.pictures)
        )
    }

    private fun unifyOrganizationPins(picture: String?): String? {
        if (picture == null) {
            return null
        }
        return picture.replace("_[0-9]{2}".toRegex(), "")
    }

    private fun organizationIsNoCandidateToImport(tOrganization: TOrganization): Boolean {
        val organizationType = tOrganization.organizationtype
        return isIrrelevantOrganizationType(organizationType)
    }

    private fun isIrrelevantOrganizationType(organizationType: TOrganizationType): Boolean {
        val irrelevantOrganizationTypes = Arrays.asList("Aktivbüro", "Sonstige", "HelfenKannJeder")
        return irrelevantOrganizationTypes.contains(organizationType.name)
    }

    private fun extractPictures(picturesString: String?): List<String> {
        return if (picturesString == null) {
            emptyList()
        } else Arrays.asList(*picturesString.split(",".toRegex()).toTypedArray())
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(Typo3OrganizationProcessor::class.java)
        private fun toAddress(tAddress: TAddress): Address = Address(
            website = UrlUnifier.unifyOrganizationWebsiteUrl(tAddress.website),
            telephone = tAddress.telephone,
            street = tAddress.street,
            addressAppendix = tAddress.addressappendix,
            city = tAddress.city,
            zipcode = tAddress.zipcode,
            location = GeoPoint(tAddress.latitude, tAddress.longitude)
        )
    }

}