package de.helfenkannjeder.helfomat.api.organisation;

import de.helfenkannjeder.helfomat.core.geopoint.BoundingBox;
import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint;
import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationRepository;
import de.helfenkannjeder.helfomat.core.organisation.QuestionAnswer;
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

    public List<OrganisationDto> findOrganisation(List<QuestionAnswerDto> questionAnswerDtos,
                                                  GeoPoint position,
                                                  double distance) {
        List<QuestionAnswer> questionAnswers = QuestionAnswerAssembler.toQuestionAnswers(questionAnswerDtos);
        if (questionAnswers == null && position == null) {
            return OrganisationAssembler.toOrganisationDtos(
                this.organisationRepository.findGlobalOrganisations()
            );
        } else if (questionAnswers == null) {
            return OrganisationAssembler.toOrganisationDtos(
                this.organisationRepository.findOrganisationsByDistanceSortByDistance(position, distance)
            );
        } else if (position == null) {
            return OrganisationAssembler.toScoredOrganisationDtos(
                this.organisationRepository.findGlobalOrganisationsByQuestionAnswersSortByAnswerMatch(questionAnswers)
            );
        } else {
            return OrganisationAssembler.toScoredOrganisationDtos(
                this.organisationRepository.findOrganisationsByQuestionAnswersAndDistanceSortByAnswerMatchAndDistance(
                    questionAnswers,
                    position,
                    distance
                )
            );
        }
    }

    public List<GeoPoint> findClusteredGeoPoints(GeoPoint position,
                                                 double distance,
                                                 BoundingBox boundingBox) {
        return this.organisationRepository.findGeoPointsOfOrganisationsInsideBoundingBox(position,
            distance,
            boundingBox);
    }

}
