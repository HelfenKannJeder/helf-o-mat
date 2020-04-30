package de.helfenkannjeder.helfomat.rest;

import de.helfenkannjeder.helfomat.api.organization.OrganizationSubmitEventDto;
import de.helfenkannjeder.helfomat.api.organization.event.OrganizationEventAssembler;
import de.helfenkannjeder.helfomat.api.organization.event.OrganizationEventDto;
import de.helfenkannjeder.helfomat.config.ImporterConfiguration;
import de.helfenkannjeder.helfomat.core.organization.Organization;
import de.helfenkannjeder.helfomat.core.organization.OrganizationId;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEvent;
import de.helfenkannjeder.helfomat.core.question.Question;
import de.helfenkannjeder.helfomat.core.question.QuestionRepository;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Valentin Zickner
 */
@Component
@EnableConfigurationProperties(ImporterConfiguration.class)
public class RestOrganizationEventPublisher {

    private static final String IMPORT_SOURCE = "Helf-O-Mat Import Application";
    private final RestTemplate restTemplate;
    private final QuestionRepository questionRepository;
    private final ImporterConfiguration importerConfiguration;

    public RestOrganizationEventPublisher(RestTemplate restTemplate, QuestionRepository questionRepository, ImporterConfiguration importerConfiguration) {
        this.restTemplate = restTemplate;
        this.questionRepository = questionRepository;
        this.importerConfiguration = importerConfiguration;
    }

    public void publishEvents(Organization organization, Stream<OrganizationEvent> events) {
        List<OrganizationEvent> organizationEvents = events.collect(Collectors.toList());
        if (organizationEvents.size() > 0) {
            OrganizationId organizationId = organization.getId();
            OrganizationSubmitEventDto organizationSubmitEventDto = toOrganizationSubmitEventDto(organizationEvents, organizationId);
            try {
                submitOrganization(organizationSubmitEventDto);
            } catch (HttpClientErrorException exception) {
                if (HttpStatus.NOT_FOUND.equals(exception.getStatusCode())) {
                    // try to create complete organization
                    submitOrganization(toOrganizationSubmitEventDto(organization.compareTo(null), organizationId));
                } else {
                    throw exception;
                }
            }
        }
    }

    private ResponseEntity<Void> submitOrganization(OrganizationSubmitEventDto organizationSubmitEventDto) {
        return this.restTemplate.postForEntity(importerConfiguration.getWebApiUrl() + "/api/organization/submit", organizationSubmitEventDto, Void.class);
    }

    private OrganizationSubmitEventDto toOrganizationSubmitEventDto(List<OrganizationEvent> organizationEvents, OrganizationId organizationId) {
        List<Question> questions = this.questionRepository.findQuestions();
        List<OrganizationEventDto> organizationEventDtos = OrganizationEventAssembler.toOrganizationEventDto(organizationEvents, questions);
        return new OrganizationSubmitEventDto(organizationId, IMPORT_SOURCE, organizationEventDtos);
    }

}
