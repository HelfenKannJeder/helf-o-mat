package de.helfenkannjeder.helfomat.api.organisation;

import de.helfenkannjeder.helfomat.api.organisation.event.OrganizationEventAssembler;
import de.helfenkannjeder.helfomat.api.organisation.event.OrganizationEventDto;
import de.helfenkannjeder.helfomat.api.organisation.event.OrganizationEventDtoAssembler;
import de.helfenkannjeder.helfomat.core.geopoint.BoundingBox;
import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint;
import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationRepository;
import de.helfenkannjeder.helfomat.core.organisation.event.ConfirmedChangeOrganizationEvent;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEvent;
import de.helfenkannjeder.helfomat.core.organisation.event.ProposedChangeOrganizationEvent;
import de.helfenkannjeder.helfomat.core.question.Question;
import de.helfenkannjeder.helfomat.core.question.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Valentin Zickner
 */
@Service
public class OrganisationApplicationService {

    private final OrganisationRepository organisationRepository;
    private final QuestionRepository questionRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public OrganisationApplicationService(OrganisationRepository organisationRepository, QuestionRepository questionRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.organisationRepository = organisationRepository;
        this.questionRepository = questionRepository;
        this.applicationEventPublisher = applicationEventPublisher;
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

    public List<OrganizationEventDto> compareOrganizations(CompareOrganizationDto compareOrganizationDto) {
        Organisation original = OrganisationAssembler.toOrganization(compareOrganizationDto.getOriginal());
        Organisation updated = OrganisationAssembler.toOrganization(compareOrganizationDto.getUpdated());
        List<Question> questions = this.questionRepository.findQuestions();
        return OrganizationEventAssembler.toOrganizationEventDto(updated.compareTo(original), questions);
    }

    @PreAuthorize("isAuthenticated()")
    public void submitOrganization(OrganizationSubmitEventDto organizationSubmitEventDto) {
        List<OrganisationEvent> organisationEvents = OrganizationEventDtoAssembler.toOrganizationEvent(organizationSubmitEventDto.getEvents());
        ProposedChangeOrganizationEvent proposedChangeOrganizationEvent = new ProposedChangeOrganizationEvent(
            organizationSubmitEventDto.getOrganizationId(),
            getCurrentUser(),
            organizationSubmitEventDto.getSources(),
            organisationEvents
        );
        applicationEventPublisher.publishEvent(proposedChangeOrganizationEvent);
    }

    @EventListener // TODO: should be based on an actual approval
    public void confirmOrganizationEvent(ProposedChangeOrganizationEvent proposedChangeOrganizationEvent) {
        ConfirmedChangeOrganizationEvent confirmedChangeOrganizationEvent = new ConfirmedChangeOrganizationEvent(
            proposedChangeOrganizationEvent.getOrganisationId(),
            getCurrentUser(),
            proposedChangeOrganizationEvent.getAuthor(),
            proposedChangeOrganizationEvent.getSources(),
            proposedChangeOrganizationEvent.getChanges()
        );
        applicationEventPublisher.publishEvent(confirmedChangeOrganizationEvent);
    }

    private String getCurrentUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
