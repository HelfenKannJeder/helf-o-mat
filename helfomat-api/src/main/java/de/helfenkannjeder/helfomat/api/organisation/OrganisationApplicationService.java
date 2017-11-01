package de.helfenkannjeder.helfomat.api.organisation;

import de.helfenkannjeder.helfomat.api.question.QuestionAnswerDto;
import de.helfenkannjeder.helfomat.core.geopoint.BoundingBox;
import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint;
import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationRepository;
import de.helfenkannjeder.helfomat.core.organisation.PictureId;
import de.helfenkannjeder.helfomat.core.question.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Valentin Zickner
 */
@Service
public class OrganisationApplicationService {

    private final OrganisationRepository organisationRepository;

    @Autowired
    public OrganisationApplicationService(OrganisationRepository organisationRepository) {
        this.organisationRepository = organisationRepository;
    }

    public Organisation findOne(String id) {
        return this.organisationRepository.findOne(id);
    }

    public List<OrganisationDto> findOrganisation(List<QuestionAnswerDto> questionAnswerDtos,
                                                  GeoPoint position,
                                                  double distance) {
        Map<String, Answer> questionAnswerMap = questionAnswerDtos.stream()
            .collect(Collectors.toMap(QuestionAnswerDto::getId, QuestionAnswerDto::getAnswer));
        return this.organisationRepository.findOrganisation(
            questionAnswerMap,
            position,
            distance
        )
            .entrySet()
            .stream()
            .map((data) -> {
                Organisation organisation = data.getKey();
                List<AddressDto> addresses = organisation.getAddresses()
                    .stream()
                    .map((address) -> new AddressDto(
                        address.getStreet(),
                        address.getAddressAppendix(),
                        address.getCity(),
                        address.getZipcode(),
                        address.getLocation(),
                        address.getTelephone(),
                        address.getWebsite()
                    ))
                    .collect(Collectors.toList());
                List<ContactPersonDto> contactPersons = organisation.getContactPersons()
                    .stream()
                    .map((contactPerson) -> new ContactPersonDto(
                        contactPerson.getFirstname(),
                        contactPerson.getLastname(),
                        contactPerson.getRank(),
                        contactPerson.getTelephone()
                    ))
                    .collect(Collectors.toList());
                return new OrganisationDto(
                    organisation.getId(),
                    organisation.getName(),
                    organisation.getDescription(),
                    organisation.getWebsite(),
                    organisation.getMapPin(),
                    addresses,
                    contactPersons,
                    pictureIdToString(organisation, organisation.getLogo()),
                    data.getValue()

                );
            })
            .collect(Collectors.toList());
    }

    private String pictureIdToString(Organisation organisation, PictureId pictureId) {
        if (pictureId == null) {
            return null;
        }
        return organisation.getLogo().getValue();
    }

    public List<GeoPoint> findClusteredGeoPoints(GeoPoint position,
                                                 double distance,
                                                 BoundingBox boundingBox) {
        return this.organisationRepository.findClusteredGeoPoints(position,
            distance,
            boundingBox);
    }

}
