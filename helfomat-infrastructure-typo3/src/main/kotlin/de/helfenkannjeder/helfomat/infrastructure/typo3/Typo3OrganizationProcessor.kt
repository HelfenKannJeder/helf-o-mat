package de.helfenkannjeder.helfomat.infrastructure.typo3;

import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint;
import de.helfenkannjeder.helfomat.core.organization.Address;
import de.helfenkannjeder.helfomat.core.organization.AttendanceTime;
import de.helfenkannjeder.helfomat.core.organization.ContactPerson;
import de.helfenkannjeder.helfomat.core.organization.Group;
import de.helfenkannjeder.helfomat.core.organization.Organization;
import de.helfenkannjeder.helfomat.core.organization.OrganizationId;
import de.helfenkannjeder.helfomat.core.organization.OrganizationType;
import de.helfenkannjeder.helfomat.core.organization.Volunteer;
import de.helfenkannjeder.helfomat.core.picture.DownloadFailedException;
import de.helfenkannjeder.helfomat.core.picture.PictureId;
import de.helfenkannjeder.helfomat.core.picture.PictureStorageService;
import de.helfenkannjeder.helfomat.infrastructure.typo3.domain.TAddress;
import de.helfenkannjeder.helfomat.infrastructure.typo3.domain.TEmployee;
import de.helfenkannjeder.helfomat.infrastructure.typo3.domain.TGroup;
import de.helfenkannjeder.helfomat.infrastructure.typo3.domain.TOrganization;
import de.helfenkannjeder.helfomat.infrastructure.typo3.domain.TOrganizationType;
import de.helfenkannjeder.helfomat.infrastructure.typo3.domain.TWorkingHour;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.Charset;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Valentin Zickner
 */
@Component
@Transactional(propagation = Propagation.REQUIRED, transactionManager = "legacyTransactionManager")
public class Typo3OrganizationProcessor implements ItemProcessor<TOrganization, Organization> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Typo3OrganizationProcessor.class);

    private final PictureStorageService pictureStorageService;

    public Typo3OrganizationProcessor(PictureStorageService pictureStorageService) {
        this.pictureStorageService = pictureStorageService;
    }

    @Override
    public Organization process(TOrganization tOrganization) {
        if (organizationIsNoCandidateToImport(tOrganization)) {
            LOGGER.info("Ignore TYPO3 organization '" + tOrganization.getName() + "'");
            return null;
        }

        return new Organization.Builder()
            .setId(new OrganizationId())
            .setName(tOrganization.getName())
            .setOrganizationType(OrganizationType.findByName(tOrganization.getOrganizationtype().getName()))
            .setDescription(tOrganization.getDescription())
            .setLogo(toPicture(tOrganization.getLogo()))
            .setWebsite(UrlUnifier.unifyOrganizationWebsiteUrl(tOrganization.getWebsite()))
            .setMapPin(unifyOrganizationPins(tOrganization.getOrganizationtype().getPicture()))
            .setPictures(toPictures(extractPictures(tOrganization.getPictures())))
            .setContactPersons(toContactPersons(tOrganization.getContactPersons()))
            .setAddresses(tOrganization.getAddresses().stream().map(Typo3OrganizationProcessor::toAddress).collect(Collectors.toList()))
            .setDefaultAddress(toAddress(tOrganization.getDefaultaddress()))
            .setGroups(
                tOrganization.getGroups().stream().map(this::toGroup).collect(Collectors.toList())
            )
            .setAttendanceTimes(tOrganization.getWorkinghours().stream().map(this::toEvent).collect(Collectors.toList()))
            .setVolunteers(tOrganization.getEmployees().stream().filter(employee -> !employee.getMotivation().isEmpty()).map(this::toVolunteer).collect(Collectors.toList()))
            .build();
    }

    private Volunteer toVolunteer(TEmployee tEmployee) {
        return new Volunteer.Builder()
            .setFirstname(tEmployee.getPrename())
            .setLastname(tEmployee.getSurname())
            .setMotivation(tEmployee.getMotivation())
            .setPicture(toPicture(tEmployee.getPictures()))
            .build();
    }

    private Group toGroup(TGroup tGroup) {
        return new Group.Builder()
            .setName(tGroup.getName())
            .setDescription(tGroup.getDescription())
            .setContactPersons(toContactPersons(tGroup.getContactPersons()))
            .setMinimumAge(tGroup.getMinimumAge())
            .setMaximumAge(tGroup.getMaximumAge())
            .setWebsite(tGroup.getWebsite())
            .build();
    }

    private AttendanceTime toEvent(TWorkingHour tWorkingHour) {
        return new AttendanceTime.Builder()
            .setDay(DayOfWeek.of(tWorkingHour.getDay()))
            .setStart(LocalTime.of(tWorkingHour.getStarttimehour(), tWorkingHour.getStarttimeminute()))
            .setEnd(LocalTime.of(tWorkingHour.getStoptimehour(), tWorkingHour.getStoptimeminute()))
            .setNote(tWorkingHour.getAddition())
            .setGroups(tWorkingHour.getGroups()
                .stream()
                .map(this::toGroup)
                .collect(Collectors.toList()))
            .build();
    }

    private static Address toAddress(TAddress tAddress) {
        if (tAddress == null) {
            return null;
        }
        return new Address.Builder()
            .setWebsite(UrlUnifier.unifyOrganizationWebsiteUrl(tAddress.getWebsite()))
            .setTelephone(tAddress.getTelephone())
            .setStreet(tAddress.getStreet())
            .setAddressAppendix(tAddress.getAddressappendix())
            .setCity(tAddress.getCity())
            .setZipcode(tAddress.getZipcode())
            .setLocation(new GeoPoint(tAddress.getLatitude(), tAddress.getLongitude()))
            .build();
    }

    private List<PictureId> toPictures(List<String> pictures) {
        return pictures
            .stream()
            .map(this::toPicture)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    PictureId toPicture(String picture) {
        if (picture == null || picture.equals("")) {
            return null;
        }
        try {
            String url = "https://helfenkannjeder.de/uploads/pics/" + picture;
            PictureId pictureId = toPictureId(url);
            if (this.pictureStorageService.existPicture(pictureId)) {
                return pictureId;
            }
            this.pictureStorageService.savePicture(
                url,
                pictureId
            );
            return pictureId;
        } catch (DownloadFailedException e) {
            LOGGER.warn("Failed to download picture", e);
            return null;
        }
    }

    private PictureId toPictureId(String url) {
        return new PictureId(UUID.nameUUIDFromBytes(url.getBytes(Charset.defaultCharset())).toString());
    }

    private List<ContactPerson> toContactPersons(List<TEmployee> employees) {
        return employees.stream()
            .map(this::toContactPerson)
            .collect(Collectors.toList());
    }

    private ContactPerson toContactPerson(TEmployee tEmployee) {
        return new ContactPerson.Builder()
            .setFirstname(tEmployee.getPrename())
            .setLastname(tEmployee.getSurname())
            .setRank(tEmployee.getRank())
            .setTelephone(tEmployee.getTelephone())
            .setMail(tEmployee.getMail())
            .setPicture(toPicture(tEmployee.getPictures()))
            .build();
    }

    private String unifyOrganizationPins(String picture) {
        return picture.replaceAll("_[0-9]{2}", "");
    }

    private boolean organizationIsNoCandidateToImport(TOrganization tOrganization) {
        TOrganizationType organizationType = tOrganization.getOrganizationtype();
        return organizationType == null || isIrrelevantOrganizationType(organizationType);
    }

    private boolean isIrrelevantOrganizationType(TOrganizationType organizationType) {
        List<String> irrelevantOrganizationTypes = Arrays.asList("Aktivbüro", "Sonstige", "HelfenKannJeder");
        return irrelevantOrganizationTypes.contains(organizationType.getName());
    }

    private List<String> extractPictures(String picturesString) {
        if (picturesString == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(picturesString.split(","));
    }

}
