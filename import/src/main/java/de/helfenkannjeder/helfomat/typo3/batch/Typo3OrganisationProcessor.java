package de.helfenkannjeder.helfomat.typo3.batch;

import de.helfenkannjeder.helfomat.domain.AddressBuilder;
import de.helfenkannjeder.helfomat.domain.GeoPoint;
import de.helfenkannjeder.helfomat.domain.Group;
import de.helfenkannjeder.helfomat.domain.Organisation;
import de.helfenkannjeder.helfomat.domain.OrganisationBuilder;
import de.helfenkannjeder.helfomat.typo3.domain.TOrganisation;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Valentin Zickner
 */
@Component
@JobScope
public class Typo3OrganisationProcessor implements ItemProcessor<TOrganisation, Organisation> {

    @Override
    public Organisation process(TOrganisation tOrganisation) throws Exception {
        // We don't want to import organisations without a specified type
        if (tOrganisation.getOrganisationtype() == null) {
            return null;
        }

        return new OrganisationBuilder()
                .setId(UUID.randomUUID().toString())
                .setName(tOrganisation.getName())
                .setType(tOrganisation.getOrganisationtype().getName())
                .setDescription(tOrganisation.getDescription())
                .setLogo(tOrganisation.getLogo())
                .setWebsite(tOrganisation.getWebsite())
                .setMapPin(tOrganisation.getOrganisationtype().getPicture())
                .setPictures(extractPictures(tOrganisation.getPictures()))
                .setAddresses(
                        tOrganisation.getAddresses().stream().map(tAddress -> new AddressBuilder()
                                .setWebsite(tAddress.getWebsite())
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

    private List<String> extractPictures(String picturesString) {
        if (picturesString == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(picturesString.split(","));
    }

}
