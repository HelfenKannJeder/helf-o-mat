package de.helfenkannjeder.helfomat.infrastructure.typo3

import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint
import de.helfenkannjeder.helfomat.core.organization.*
import de.helfenkannjeder.helfomat.core.picture.DownloadFailedException
import de.helfenkannjeder.helfomat.core.picture.PictureId
import de.helfenkannjeder.helfomat.core.picture.PictureStorageService
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
        return Organization.Builder()
            .setId(OrganizationId())
            .setName(tOrganization.name)
            .setOrganizationType(OrganizationType.findByName(tOrganization.organizationtype?.name))
            .setDescription(tOrganization.description)
            .setLogo(toPicture(tOrganization.logo))
            .setWebsite(UrlUnifier.unifyOrganizationWebsiteUrl(tOrganization.website))
            .setMapPin(unifyOrganizationPins(tOrganization.organizationtype?.picture))
            .setPictures(toPictures(extractPictures(tOrganization.pictures)))
            .setContactPersons(toContactPersons(tOrganization.contactPersons))
            .setAddresses(tOrganization.addresses?.map { tAddress: TAddress? -> toAddress(tAddress) })
            .setDefaultAddress(toAddress(tOrganization.defaultaddress))
            .setGroups(
                tOrganization.groups.stream().map { tGroup: TGroup -> toGroup(tGroup) }.collect(Collectors.toList())
            )
            .setAttendanceTimes(tOrganization.workinghours.map { toEvent(it) })
            .setVolunteers(tOrganization.employees.filter { !(it.motivation?.isEmpty() ?: true) }.map { toVolunteer(it) })
            .build()
    }

    private fun toVolunteer(tEmployee: TEmployee): Volunteer {
        return Volunteer(
            firstname = tEmployee.prename ?: "",
            lastname = tEmployee.surname,
            motivation = tEmployee.motivation ?: "",
            picture = toPicture(tEmployee.pictures)
        )
    }

    private fun toGroup(tGroup: TGroup): Group {
        return Group.Builder()
            .setName(tGroup.name)
            .setDescription(tGroup.description)
            .setContactPersons(toContactPersons(tGroup.contactPersons))
            .setMinimumAge(tGroup.minimumAge)
            .setMaximumAge(tGroup.maximumAge)
            .setWebsite(tGroup.website)
            .build()
    }

    private fun toEvent(tWorkingHour: TWorkingHour): AttendanceTime {
        return AttendanceTime.Builder()
            .setDay(DayOfWeek.of(tWorkingHour.day))
            .setStart(LocalTime.of(tWorkingHour.starttimehour, tWorkingHour.starttimeminute))
            .setEnd(LocalTime.of(tWorkingHour.stoptimehour, tWorkingHour.stoptimeminute))
            .setNote(tWorkingHour.addition)
            .setGroups(tWorkingHour.groups
                .stream()
                .map { tGroup: TGroup -> toGroup(tGroup) }
                .collect(Collectors.toList()))
            .build()
    }

    private fun toPictures(pictures: List<String>): List<PictureId> {
        return pictures
            .map { picture: String -> toPicture(picture) }
            .filterNotNull()
    }

    fun toPicture(picture: String?): PictureId? {
        return if (picture == null || picture == "") {
            null
        } else try {
            val url = "https://helfenkannjeder.de/uploads/pics/$picture"
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
        return ContactPerson.Builder()
            .setFirstname(tEmployee.prename)
            .setLastname(tEmployee.surname)
            .setRank(tEmployee.rank)
            .setTelephone(tEmployee.telephone)
            .setMail(tEmployee.mail)
            .setPicture(toPicture(tEmployee.pictures))
            .build()
    }

    private fun unifyOrganizationPins(picture: String?): String? {
        if (picture == null) {
            return null
        }
        return picture.replace("_[0-9]{2}".toRegex(), "")
    }

    private fun organizationIsNoCandidateToImport(tOrganization: TOrganization): Boolean {
        val organizationType = tOrganization.organizationtype
        return organizationType == null || isIrrelevantOrganizationType(organizationType)
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
        private fun toAddress(tAddress: TAddress?): Address? {
            return if (tAddress == null) {
                null
            } else Address.Builder()
                .setWebsite(UrlUnifier.unifyOrganizationWebsiteUrl(tAddress.website))
                .setTelephone(tAddress.telephone)
                .setStreet(tAddress.street)
                .setAddressAppendix(tAddress.addressappendix)
                .setCity(tAddress.city)
                .setZipcode(tAddress.zipcode)
                .setLocation(GeoPoint(tAddress.latitude, tAddress.longitude))
                .build()
        }
    }

}