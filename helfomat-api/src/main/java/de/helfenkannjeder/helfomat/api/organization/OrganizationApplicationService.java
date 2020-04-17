package de.helfenkannjeder.helfomat.api.organization;

import de.helfenkannjeder.helfomat.api.organization.event.OrganizationEventAssembler;
import de.helfenkannjeder.helfomat.api.organization.event.OrganizationEventDto;
import de.helfenkannjeder.helfomat.api.organization.event.OrganizationEventDtoAssembler;
import de.helfenkannjeder.helfomat.core.geopoint.BoundingBox;
import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint;
import de.helfenkannjeder.helfomat.core.organization.Organization;
import de.helfenkannjeder.helfomat.core.organization.OrganizationRepository;
import de.helfenkannjeder.helfomat.core.organization.event.ConfirmedChangeOrganizationEvent;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEvent;
import de.helfenkannjeder.helfomat.core.organization.event.ProposedChangeOrganizationEvent;
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
public class OrganizationApplicationService {

    private final OrganizationRepository organizationRepository;
    private final QuestionRepository questionRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public OrganizationApplicationService(OrganizationRepository organizationRepository, QuestionRepository questionRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.organizationRepository = organizationRepository;
        this.questionRepository = questionRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public OrganizationDetailDto findOrganizationDetails(String urlName) {
        Organization organization = this.organizationRepository.findByUrlName(urlName);
        List<Question> questions = this.questionRepository.findQuestions();
        return OrganizationAssembler.toOrganizationDetailDto(organization, questions);
    }

    public List<OrganizationDto> findGlobalOrganizations() {
        return OrganizationAssembler.toOrganizationDtos(
            this.organizationRepository.findGlobalOrganizations()
        );
    }

    public List<OrganizationDto> findGlobalOrganizationsWith(List<QuestionAnswerDto> questionAnswerDtos) {
        return OrganizationAssembler.toScoredOrganizationDtos(
            this.organizationRepository.findGlobalOrganizationsByQuestionAnswersSortByAnswerMatch(QuestionAnswerAssembler.toQuestionAnswers(questionAnswerDtos))
        );
    }

    public List<OrganizationDto> findOrganizationsWith(GeoPoint position, double distance) {
        return OrganizationAssembler.toOrganizationDtos(
            this.organizationRepository.findOrganizationsByDistanceSortByDistance(position, distance)
        );
    }

    public List<OrganizationDto> findOrganizationsWith(List<QuestionAnswerDto> questionAnswerDtos, GeoPoint position, double distance) {
        return OrganizationAssembler.toScoredOrganizationDtos(
            this.organizationRepository.findOrganizationsByQuestionAnswersAndDistanceSortByAnswerMatchAndDistance(
                QuestionAnswerAssembler.toQuestionAnswers(questionAnswerDtos),
                position,
                distance
            )
        );
    }

    public List<GeoPoint> findClusteredGeoPoints(GeoPoint position,
                                                 double distance,
                                                 BoundingBox boundingBox) {
        return this.organizationRepository.findGeoPointsOfOrganizationsInsideBoundingBox(position,
            distance,
            boundingBox);
    }

    public List<OrganizationEventDto> compareOrganizations(CompareOrganizationDto compareOrganizationDto) {
        Organization original = OrganizationAssembler.toOrganization(compareOrganizationDto.getOriginal());
        Organization updated = OrganizationAssembler.toOrganization(compareOrganizationDto.getUpdated());
        List<Question> questions = this.questionRepository.findQuestions();
        return OrganizationEventAssembler.toOrganizationEventDto(updated.compareTo(original), questions);
    }

    @PreAuthorize("isAuthenticated()")
    public void submitOrganization(OrganizationSubmitEventDto organizationSubmitEventDto) {
        List<OrganizationEvent> organizationEvents = OrganizationEventDtoAssembler.toOrganizationEvent(organizationSubmitEventDto.getEvents());
        ProposedChangeOrganizationEvent proposedChangeOrganizationEvent = new ProposedChangeOrganizationEvent(
            organizationSubmitEventDto.getOrganizationId(),
            getCurrentUser(),
            organizationSubmitEventDto.getSources(),
            organizationEvents
        );
        applicationEventPublisher.publishEvent(proposedChangeOrganizationEvent);
    }

    @EventListener // TODO: should be based on an actual approval
    public void confirmOrganizationEvent(ProposedChangeOrganizationEvent proposedChangeOrganizationEvent) {
        ConfirmedChangeOrganizationEvent confirmedChangeOrganizationEvent = new ConfirmedChangeOrganizationEvent(
            proposedChangeOrganizationEvent.getOrganizationId(),
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
