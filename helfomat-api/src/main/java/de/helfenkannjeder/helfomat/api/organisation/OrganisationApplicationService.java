package de.helfenkannjeder.helfomat.api.organisation;

import de.helfenkannjeder.helfomat.api.question.QuestionAnswerDto;
import de.helfenkannjeder.helfomat.core.geopoint.BoundingBox;
import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint;
import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationRepository;
import de.helfenkannjeder.helfomat.core.question.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
        if (position == null) {
            // TODO: return organisation without address: Those are the general organisations which
            // are not specialized and should be used here. Unfortunately, there is currently no way
            // to identify those organisations.
            return Collections.emptyList();
        }


        Map<String, Answer> questionAnswerMap = questionAnswerDtos.stream()
            .collect(Collectors.toMap(QuestionAnswerDto::getId, QuestionAnswerDto::getAnswer));
        return this.organisationRepository.findOrganisation(
            questionAnswerMap,
            position,
            distance
        )
            .entrySet()
            .stream()
            .map(data -> OrganisationAssembler.toOrganisationDto(data.getKey(), data.getValue()))
            .collect(Collectors.toList());
    }

    public List<GeoPoint> findClusteredGeoPoints(GeoPoint position,
                                                 double distance,
                                                 BoundingBox boundingBox) {
        return this.organisationRepository.findClusteredGeoPoints(position,
            distance,
            boundingBox);
    }

}
