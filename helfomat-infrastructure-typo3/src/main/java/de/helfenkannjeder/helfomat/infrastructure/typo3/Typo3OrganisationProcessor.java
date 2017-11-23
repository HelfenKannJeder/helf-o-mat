package de.helfenkannjeder.helfomat.infrastructure.typo3;

import de.helfenkannjeder.helfomat.core.IndexManager;
import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint;
import de.helfenkannjeder.helfomat.core.organisation.Address;
import de.helfenkannjeder.helfomat.core.organisation.AttendanceTime;
import de.helfenkannjeder.helfomat.core.organisation.ContactPerson;
import de.helfenkannjeder.helfomat.core.organisation.Group;
import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationType;
import de.helfenkannjeder.helfomat.core.organisation.Volunteer;
import de.helfenkannjeder.helfomat.core.picture.DownloadFailedException;
import de.helfenkannjeder.helfomat.core.picture.PictureId;
import de.helfenkannjeder.helfomat.core.picture.PictureRepository;
import de.helfenkannjeder.helfomat.infrastructure.typo3.domain.TAddress;
import de.helfenkannjeder.helfomat.infrastructure.typo3.domain.TEmployee;
import de.helfenkannjeder.helfomat.infrastructure.typo3.domain.TGroup;
import de.helfenkannjeder.helfomat.infrastructure.typo3.domain.TOrganisation;
import de.helfenkannjeder.helfomat.infrastructure.typo3.domain.TOrganisationType;
import de.helfenkannjeder.helfomat.infrastructure.typo3.domain.TWorkingHour;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

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
@JobScope
public class Typo3OrganisationProcessor implements ItemProcessor<TOrganisation, Organisation> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Typo3OrganisationProcessor.class);

    private final PictureRepository pictureRepository;
    private final IndexManager indexManager;

    public Typo3OrganisationProcessor(PictureRepository pictureRepository, IndexManager indexManager) {
        this.pictureRepository = pictureRepository;
        this.indexManager = indexManager;
    }

    @Override
    public Organisation process(TOrganisation tOrganisation) {
        if (organisationIsNoCandidateToImport(tOrganisation)) {
            LOGGER.info("Ignore TYPO3 organisation '" + tOrganisation.getName() + "'");
            return null;
        }

        return new Organisation.Builder()
            .setId(UUID.randomUUID().toString())
            .setName(tOrganisation.getName())
            .setOrganisationType(OrganisationType.findByName(tOrganisation.getOrganisationtype().getName()))
            .setDescription(tOrganisation.getDescription())
            .setLogo(toPicture(tOrganisation.getLogo()))
            .setWebsite(UrlUnifier.unifyOrganisationWebsiteUrl(tOrganisation.getWebsite()))
            .setMapPin(unifyOrganisationPins(tOrganisation.getOrganisationtype().getPicture()))
            .setPictures(toPictures(extractPictures(tOrganisation.getPictures())))
            .setContactPersons(toContactPersons(tOrganisation.getContactPersons()))
            .setAddresses(tOrganisation.getAddresses().stream().map(Typo3OrganisationProcessor::toAddress).collect(Collectors.toList()))
            .setDefaultAddress(toAddress(tOrganisation.getDefaultaddress()))
            .setGroups(
                tOrganisation.getGroups().stream().map(this::toGroup).collect(Collectors.toList())
            )
            .setAttendanceTimes(tOrganisation.getWorkinghours().stream().map(this::toEvent).collect(Collectors.toList()))
            .setVolunteers(tOrganisation.getEmployees().stream().filter(employee -> !employee.getMotivation().isEmpty()).map(this::toVolunteer).collect(Collectors.toList()))
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
            .setWebsite(UrlUnifier.unifyOrganisationWebsiteUrl(tAddress.getWebsite()))
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

    private PictureId toPicture(String picture) {
        if (picture == null || picture.equals("")) {
            return null;
        }
        try {
            return this.pictureRepository.savePicture(
                "https://helfenkannjeder.de/uploads/pics/" + picture,
                this.indexManager.getCurrentIndex(),
                new PictureId()
            );
        } catch (DownloadFailedException e) {
            LOGGER.warn("Failed to download picture", e);
            return null;
        }
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

    private String unifyOrganisationPins(String picture) {
        return picture.replaceAll("_[0-9]{2}", "");
    }

    private boolean organisationIsNoCandidateToImport(TOrganisation tOrganisation) {
        TOrganisationType organisationType = tOrganisation.getOrganisationtype();
        return organisationType == null || isIrrelevantOrganisationType(organisationType);
    }

    private boolean isIrrelevantOrganisationType(TOrganisationType organisationType) {
        List<String> irrelevantOrganisationTypes = Arrays.asList("Aktivbüro", "Sonstige", "HelfenKannJeder");
        return irrelevantOrganisationTypes.contains(organisationType.getName());
    }

    private List<String> extractPictures(String picturesString) {
        if (picturesString == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(picturesString.split(","));
    }

}
