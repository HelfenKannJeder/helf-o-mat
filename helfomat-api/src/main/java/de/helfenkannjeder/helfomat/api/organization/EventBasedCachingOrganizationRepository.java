package de.helfenkannjeder.helfomat.api.organization;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.helfenkannjeder.helfomat.core.geopoint.BoundingBox;
import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint;
import de.helfenkannjeder.helfomat.core.organization.Organization;
import de.helfenkannjeder.helfomat.core.organization.OrganizationId;
import de.helfenkannjeder.helfomat.core.organization.OrganizationRepository;
import de.helfenkannjeder.helfomat.core.organization.QuestionAnswer;
import de.helfenkannjeder.helfomat.core.organization.ScoredOrganization;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Valentin Zickner
 */
public class EventBasedCachingOrganizationRepository implements OrganizationRepository {

    private final static Logger LOG = LoggerFactory.getLogger(EventBasedCachingOrganizationRepository.class);

    protected final Map<OrganizationId, Organization.Builder> organizationBuilderMap = new HashMap<>();
    protected final ObjectMapper objectMapper;
    protected final OrganizationRepository persistentOrganizationRepository;

    public EventBasedCachingOrganizationRepository(ObjectMapper objectMapper, OrganizationRepository persistentOrganizationRepository) {
        this.objectMapper = objectMapper;
        this.persistentOrganizationRepository = persistentOrganizationRepository;
    }

    protected void processDomainEvents(OrganizationId organizationId, List<OrganizationEvent> organizationEvents) {
        LOG.debug("Received organization events for organization '{}' from event storage '{}'", organizationId, organizationEvents);
        Organization.Builder organizationBuilder = organizationBuilderMap.getOrDefault(organizationId, new Organization.Builder());
        for (OrganizationEvent organizationEvent : organizationEvents) {
            organizationBuilder = organizationEvent.applyOnOrganizationBuilder(organizationBuilder);
        }
        organizationBuilderMap.put(organizationId, organizationBuilder);
        this.persistentOrganizationRepository.save(Collections.singletonList(organizationBuilder.build()));
    }

    protected void processDomainEvent(OrganizationEvent organizationEvent) {
        OrganizationId organizationId = organizationEvent.getOrganizationId();
        LOG.debug("Received organization event for organization '{}' from event storage '{}'", organizationId, organizationEvent);
        Organization.Builder organizationBuilder = organizationEvent.applyOnOrganizationBuilder(
            organizationBuilderMap.getOrDefault(organizationId, new Organization.Builder())
        );
        organizationBuilderMap.put(organizationId, organizationBuilder);
        this.persistentOrganizationRepository.save(Collections.singletonList(organizationBuilder.build()));
    }

    @Override
    public boolean existsOrganizationWithSameTypeInDistance(Organization organization, Long distanceInMeters) {
        return this.persistentOrganizationRepository.existsOrganizationWithSameTypeInDistance(organization, distanceInMeters);
    }

    @Override
    public Organization findOrganizationWithSameTypeInDistance(Organization organization, Long distanceInMeters) {
        return this.persistentOrganizationRepository.findOrganizationWithSameTypeInDistance(organization, distanceInMeters);
    }

    @Override
    public Organization findByUrlName(String urlName) {
        return this.persistentOrganizationRepository.findByUrlName(urlName);
    }

    @Override
    public Organization findOne(String id) {
        return this.persistentOrganizationRepository.findOne(id);
    }

    @Override
    public List<ScoredOrganization> findOrganizationsByQuestionAnswersAndDistanceSortByAnswerMatchAndDistance(List<QuestionAnswer> questionAnswers, GeoPoint position, double distance) {
        return this.persistentOrganizationRepository.findOrganizationsByQuestionAnswersAndDistanceSortByAnswerMatchAndDistance(questionAnswers, position, distance);
    }

    @Override
    public List<Organization> findOrganizationsByDistanceSortByDistance(GeoPoint position, double distance) {
        return this.persistentOrganizationRepository.findOrganizationsByDistanceSortByDistance(position, distance);
    }

    @Override
    public List<ScoredOrganization> findGlobalOrganizationsByQuestionAnswersSortByAnswerMatch(List<QuestionAnswer> questionAnswers) {
        return this.persistentOrganizationRepository.findGlobalOrganizationsByQuestionAnswersSortByAnswerMatch(questionAnswers);
    }

    @Override
    public List<Organization> findGlobalOrganizations() {
        return this.persistentOrganizationRepository.findGlobalOrganizations();
    }

    @Override
    public List<GeoPoint> findGeoPointsOfOrganizationsInsideBoundingBox(GeoPoint position, double distance, BoundingBox boundingBox) {
        return this.persistentOrganizationRepository.findGeoPointsOfOrganizationsInsideBoundingBox(position, distance, boundingBox);
    }

    @Override
    public void save(List<? extends Organization> organizations) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void createIndex(String mapping) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteIndex() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void updateAlias(String alias) {
        throw new UnsupportedOperationException();
    }
}
