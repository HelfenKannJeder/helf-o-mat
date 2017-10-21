package de.helfenkannjeder.helfomat.infrastructure.typo3;

import de.helfekannjeder.helfomat.core.IndexManager;
import de.helfekannjeder.helfomat.core.organisation.Address;
import de.helfekannjeder.helfomat.core.organisation.ContactPerson;
import de.helfekannjeder.helfomat.core.organisation.GeoPoint;
import de.helfekannjeder.helfomat.core.organisation.Group;
import de.helfekannjeder.helfomat.core.organisation.Organisation;
import de.helfekannjeder.helfomat.core.organisation.OrganisationType;
import de.helfekannjeder.helfomat.core.organisation.PictureId;
import de.helfekannjeder.helfomat.core.picture.DownloadFailedException;
import de.helfekannjeder.helfomat.core.picture.PictureRepository;
import de.helfenkannjeder.helfomat.infrastructure.typo3.domain.TEmployee;
import de.helfenkannjeder.helfomat.infrastructure.typo3.domain.TOrganisation;
import de.helfenkannjeder.helfomat.infrastructure.typo3.domain.TOrganisationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static de.helfekannjeder.helfomat.core.organisation.OrganisationType.Aktivbuero;
import static de.helfekannjeder.helfomat.core.organisation.OrganisationType.Helfenkannjeder;
import static de.helfekannjeder.helfomat.core.organisation.OrganisationType.Sonstige;

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
            return null;
        }

        return new Organisation.Builder()
            .setId(UUID.randomUUID().toString())
            .setName(tOrganisation.getName())
            .setType(tOrganisation.getOrganisationtype().getName())
            .setDescription(tOrganisation.getDescription())
            .setLogo(toPicture(tOrganisation.getLogo()))
            .setWebsite(UrlUnifier.unifyOrganisationWebsiteUrl(tOrganisation.getWebsite()))
            .setMapPin(unifyOrganisationPins(tOrganisation.getOrganisationtype().getPicture()))
            .setPictures(toPictures(extractPictures(tOrganisation.getPictures())))
            .setContactPersons(extractContactPersons(tOrganisation.getEmployees()))
            .setAddresses(
                tOrganisation.getAddresses().stream().map(tAddress -> new Address.Builder()
                    .setWebsite(UrlUnifier.unifyOrganisationWebsiteUrl(tAddress.getWebsite()))
                    .setTelephone(tAddress.getTelephone())
                    .setStreet(tAddress.getStreet())
                    .setAddressAppendix(tAddress.getAddressappendix())
                    .setCity(tAddress.getCity())
                    .setZipcode(tAddress.getZipcode())
                    .setLocation(new GeoPoint(tAddress.getLatitude(), tAddress.getLongitude()))
                    .build()).collect(Collectors.toList())
            )
            .setGroups(
                tOrganisation.getGroups().stream().map(tGroup -> {
                    Group group = new Group();
                    group.setName(tGroup.getName());
                    group.setDescription(tGroup.getDescription());
                    return group;
                }).collect(Collectors.toList())
            )
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
            LOGGER.warn("Failed to donwload picture", e);
            return null;
        }
    }

    private static List<ContactPerson> extractContactPersons(List<TEmployee> employees) {
        return employees.stream()
            .filter(TEmployee::isIscontact)
            .map(Typo3OrganisationProcessor::toContactPerson)
            .collect(Collectors.toList());
    }

    private static ContactPerson toContactPerson(TEmployee tEmployee) {
        return new ContactPerson.Builder()
            .setFirstname(tEmployee.getPrename())
            .setLastname(tEmployee.getSurname())
            .setRank(tEmployee.getRank())
            .setTelephone(tEmployee.getTelephone())
            .setMail(tEmployee.getMail())
            .build();
    }

    private String unifyOrganisationPins(String picture) {
        return picture.replaceAll("_[0-9]{2}", "");
    }

    private boolean organisationIsNoCandidateToImport(TOrganisation tOrganisation) {
        // We don't want to import organisations without a specified type
        if (tOrganisation.getOrganisationtype() == null) {
            return true;
        }

        List<OrganisationType> irrelevantOrganisationTypes = Arrays.asList(Aktivbuero, Sonstige, Helfenkannjeder);
        TOrganisationType organisationType = tOrganisation.getOrganisationtype();
        if (irrelevantOrganisationTypes.stream().map(OrganisationType::toString).anyMatch(item -> organisationType.getName().equals(item))) {
            return true;
        }

        return false;
    }

    private List<String> extractPictures(String picturesString) {
        if (picturesString == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(picturesString.split(","));
    }

}
