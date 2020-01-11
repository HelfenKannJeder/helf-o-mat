package de.helfenkannjeder.helfomat.infrastructure.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.helfenkannjeder.helfomat.core.geopoint.BoundingBox;
import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint;
import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationRepository;
import de.helfenkannjeder.helfomat.core.organisation.QuestionAnswer;
import de.helfenkannjeder.helfomat.core.organisation.ScoredOrganisation;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Valentin Zickner
 */
public class KafkaOrganisationRepository implements OrganisationRepository {

    private final static Logger LOG = LoggerFactory.getLogger(KafkaOrganisationRepository.class);

    private final Map<OrganisationId, Organisation.Builder> organisationBuilderMap = new HashMap<>();
    private final ObjectMapper objectMapper;
    private final OrganisationRepository persistentOrganisationRepository;

    public KafkaOrganisationRepository(ObjectMapper objectMapper, OrganisationRepository persistentOrganisationRepository) {
        this.objectMapper = objectMapper;
        this.persistentOrganisationRepository = persistentOrganisationRepository;
    }

    @KafkaListener(topics = "${kafka.topic.organisation-events}")
    public void listen(byte[] organisationEventByteArray) throws IOException {
        OrganisationEvent organisationEvent = this.objectMapper.readValue(organisationEventByteArray, OrganisationEvent.class);
        OrganisationId organisationId = organisationEvent.getOrganisationId();
        LOG.debug("Received organisation event for organisation '{}' from kafka '{}'", organisationId, organisationEvent);
        Organisation.Builder organisationBuilder = organisationEvent.applyOnOrganisationBuilder(
            organisationBuilderMap.getOrDefault(organisationId, new Organisation.Builder())
        );
        organisationBuilderMap.put(organisationId, organisationBuilder);
        this.persistentOrganisationRepository.save(Collections.singletonList(organisationBuilder.build()));
    }

    @Override
    public boolean existsOrganisationWithSameTypeInDistance(Organisation organisation, Long distanceInMeters) {
        return this.persistentOrganisationRepository.existsOrganisationWithSameTypeInDistance(organisation, distanceInMeters);
    }

    @Override
    public Organisation findOrganisationWithSameTypeInDistance(Organisation organisation, Long distanceInMeters) {
        return this.persistentOrganisationRepository.findOrganisationWithSameTypeInDistance(organisation, distanceInMeters);
    }

    @Override
    public Organisation findByUrlName(String urlName) {
        return this.persistentOrganisationRepository.findByUrlName(urlName);
    }

    @Override
    public Organisation findOne(String id) {
        return this.persistentOrganisationRepository.findOne(id);
    }

    @Override
    public List<ScoredOrganisation> findOrganisationsByQuestionAnswersAndDistanceSortByAnswerMatchAndDistance(List<QuestionAnswer> questionAnswers, GeoPoint position, double distance) {
        return this.persistentOrganisationRepository.findOrganisationsByQuestionAnswersAndDistanceSortByAnswerMatchAndDistance(questionAnswers, position, distance);
    }

    @Override
    public List<Organisation> findOrganisationsByDistanceSortByDistance(GeoPoint position, double distance) {
        return this.persistentOrganisationRepository.findOrganisationsByDistanceSortByDistance(position, distance);
    }

    @Override
    public List<ScoredOrganisation> findGlobalOrganisationsByQuestionAnswersSortByAnswerMatch(List<QuestionAnswer> questionAnswers) {
        return this.persistentOrganisationRepository.findGlobalOrganisationsByQuestionAnswersSortByAnswerMatch(questionAnswers);
    }

    @Override
    public List<Organisation> findGlobalOrganisations() {
        return this.persistentOrganisationRepository.findGlobalOrganisations();
    }

    @Override
    public List<GeoPoint> findGeoPointsOfOrganisationsInsideBoundingBox(GeoPoint position, double distance, BoundingBox boundingBox) {
        return this.persistentOrganisationRepository.findGeoPointsOfOrganisationsInsideBoundingBox(position, distance, boundingBox);
    }

    @Override
    public void save(List<? extends Organisation> organisations) {
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
