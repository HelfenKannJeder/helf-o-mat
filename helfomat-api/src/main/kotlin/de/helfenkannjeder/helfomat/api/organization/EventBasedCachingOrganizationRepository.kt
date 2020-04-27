package de.helfenkannjeder.helfomat.api.organization;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.helfenkannjeder.helfomat.core.geopoint.BoundingBox;
import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint;
import de.helfenkannjeder.helfomat.core.organization.Address;
import de.helfenkannjeder.helfomat.core.organization.Organization;
import de.helfenkannjeder.helfomat.core.organization.OrganizationId;
import de.helfenkannjeder.helfomat.core.organization.OrganizationRepository;
import de.helfenkannjeder.helfomat.core.organization.OrganizationType;
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
        Organization.Builder organizationBuilder = getExistingOrganizationBuilder(organizationId);
        for (OrganizationEvent organizationEvent : organizationEvents) {
            organizationBuilder = organizationEvent.applyOnOrganizationBuilder(organizationBuilder);
        }
        saveToLocalCache(organizationId, organizationBuilder);
        this.persistentOrganizationRepository.save(Collections.singletonList(organizationBuilder.build()));
    }

    protected void processDomainEvent(OrganizationEvent organizationEvent) {
        OrganizationId organizationId = organizationEvent.getOrganizationId();
        LOG.debug("Received organization event for organization '{}' from event storage '{}'", organizationId, organizationEvent);
        Organization.Builder organizationBuilder = organizationEvent.applyOnOrganizationBuilder(
            getExistingOrganizationBuilder(organizationId)
        );
        saveToLocalCache(organizationId, organizationBuilder);
        this.persistentOrganizationRepository.save(Collections.singletonList(organizationBuilder.build()));
    }

    protected Organization.Builder getExistingOrganizationBuilder(OrganizationId organizationId) {
        return organizationBuilderMap.getOrDefault(organizationId, new Organization.Builder());
    }

    protected void saveToLocalCache(OrganizationId organizationId, Organization.Builder organizationBuilder) {
        organizationBuilderMap.put(organizationId, organizationBuilder);
    }

    @Override
    public List<Organization> findOrganizationWithSameTypeInDistance(Address defaultAddress, OrganizationType organizationType, Long distanceInMeters) {
        return this.persistentOrganizationRepository.findOrganizationWithSameTypeInDistance(defaultAddress, organizationType, distanceInMeters);
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

}
