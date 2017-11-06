package de.helfenkannjeder.helfomat.api.organisation;

import de.helfenkannjeder.helfomat.core.organisation.Address;
import de.helfenkannjeder.helfomat.core.organisation.ContactPerson;
import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.picture.PictureId;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Valentin Zickner
 */
class OrganisationAssembler {

    static OrganisationDto toOrganisationDto(Organisation organisation, Float scoreNorm) {
        return new OrganisationDto(
            organisation.getId(),
            organisation.getName(),
            organisation.getDescription(),
            organisation.getWebsite(),
            organisation.getMapPin(),
            toAddressDtos(organisation),
            toContactPersonDtos(organisation),
            pictureIdToString(organisation, organisation.getLogo()),
            scoreNorm

        );
    }

    private static List<ContactPersonDto> toContactPersonDtos(Organisation organisation) {
        return organisation.getContactPersons()
            .stream()
            .map(OrganisationAssembler::toContactPersonDto)
            .collect(Collectors.toList());
    }

    private static ContactPersonDto toContactPersonDto(ContactPerson contactPerson) {
        return new ContactPersonDto(
            contactPerson.getFirstname(),
            contactPerson.getLastname(),
            contactPerson.getRank(),
            contactPerson.getTelephone()
        );
    }

    private static List<AddressDto> toAddressDtos(Organisation organisation) {
        return organisation.getAddresses()
            .stream()
            .map(OrganisationAssembler::toAddressDto)
            .collect(Collectors.toList());
    }

    private static AddressDto toAddressDto(Address address) {
        return new AddressDto(
            address.getStreet(),
            address.getAddressAppendix(),
            address.getCity(),
            address.getZipcode(),
            address.getLocation(),
            address.getTelephone(),
            address.getWebsite()
        );
    }

    private static String pictureIdToString(Organisation organisation, PictureId pictureId) {
        if (pictureId == null) {
            return null;
        }
        return organisation.getLogo().getValue();
    }
}
