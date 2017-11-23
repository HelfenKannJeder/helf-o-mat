package de.helfenkannjeder.helfomat.api.organisation;

import de.helfenkannjeder.helfomat.core.geopoint.BoundingBox;
import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint;
import de.helfenkannjeder.helfomat.core.organisation.Answer;
import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationRepository;
import de.helfenkannjeder.helfomat.core.organisation.ScoredOrganisation;
import de.helfenkannjeder.helfomat.core.question.Question;
import de.helfenkannjeder.helfomat.core.question.QuestionRepository;
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
    private final QuestionRepository questionRepository;

    @Autowired
    public OrganisationApplicationService(OrganisationRepository organisationRepository, QuestionRepository questionRepository) {
        this.organisationRepository = organisationRepository;
        this.questionRepository = questionRepository;
    }

    public OrganisationDetailDto findOrganisationDetails(String urlName) {
        Organisation organisation = this.organisationRepository.findByUrlName(urlName);
        List<Question> questions = this.questionRepository.findQuestions();
        return OrganisationAssembler.toOrganisationDetailDto(organisation, questions);
    }

    public List<OrganisationDto> findOrganisation(List<QuestionAnswerDto> questionAnswerDtos,
                                                  GeoPoint position,
                                                  double distance) {
        Map<String, Answer> questionAnswerMap = questionAnswerDtos.stream()
            .collect(Collectors.toMap(QuestionAnswerDto::getId, QuestionAnswerDto::getAnswer));
        List<ScoredOrganisation> organisations;
        if (position == null) {
            organisations = this.organisationRepository.findGlobalOrganisations(questionAnswerMap);
        } else {
            organisations = this.organisationRepository.findOrganisations(
                questionAnswerMap,
                position,
                distance
            );
        }
        return toOrganisationDtos(organisations);
    }

    private List<OrganisationDto> toOrganisationDtos(List<ScoredOrganisation> organisations) {
        return organisations
            .stream()
            .map(OrganisationAssembler::toOrganisationDto)
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
