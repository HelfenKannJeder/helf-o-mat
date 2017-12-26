package de.helfenkannjeder.helfomat.api.organisation;

import de.helfenkannjeder.helfomat.core.geopoint.BoundingBox;
import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint;
import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationRepository;
import de.helfenkannjeder.helfomat.core.question.Question;
import de.helfenkannjeder.helfomat.core.question.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<OrganisationDto> findGlobalOrganisations() {
        return OrganisationAssembler.toOrganisationDtos(
            this.organisationRepository.findGlobalOrganisations()
        );
    }

    public List<OrganisationDto> findGlobalOrganisationsWith(List<QuestionAnswerDto> questionAnswerDtos) {
        return OrganisationAssembler.toScoredOrganisationDtos(
            this.organisationRepository.findGlobalOrganisationsByQuestionAnswersSortByAnswerMatch(QuestionAnswerAssembler.toQuestionAnswers(questionAnswerDtos))
        );
    }

    public List<OrganisationDto> findOrganisationsWith(GeoPoint position, double distance) {
        return OrganisationAssembler.toOrganisationDtos(
            this.organisationRepository.findOrganisationsByDistanceSortByDistance(position, distance)
        );
    }

    public List<OrganisationDto> findOrganisationsWith(List<QuestionAnswerDto> questionAnswerDtos, GeoPoint position, double distance) {
        return OrganisationAssembler.toScoredOrganisationDtos(
            this.organisationRepository.findOrganisationsByQuestionAnswersAndDistanceSortByAnswerMatchAndDistance(
                QuestionAnswerAssembler.toQuestionAnswers(questionAnswerDtos),
                position,
                distance
            )
        );
    }

    public List<GeoPoint> findClusteredGeoPoints(GeoPoint position,
                                                 double distance,
                                                 BoundingBox boundingBox) {
        return this.organisationRepository.findGeoPointsOfOrganisationsInsideBoundingBox(position,
            distance,
            boundingBox);
    }

}
