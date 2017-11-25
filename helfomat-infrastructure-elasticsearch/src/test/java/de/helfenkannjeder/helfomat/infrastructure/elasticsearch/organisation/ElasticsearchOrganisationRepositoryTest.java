package de.helfenkannjeder.helfomat.infrastructure.elasticsearch.organisation;

import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint;
import de.helfenkannjeder.helfomat.core.organisation.Address;
import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationType;
import de.helfenkannjeder.helfomat.infrastructure.elasticsearch.ElasticsearchConfiguration;
import org.elasticsearch.client.Client;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StreamUtils;

import java.nio.charset.Charset;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Valentin Zickner
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ElasticsearchOrganisationRepositoryTest {

    @Autowired
    ElasticsearchConfiguration elasticsearchConfiguration;

    @Autowired
    ElasticsearchOrganisationRepository organisationRepository;

    @Autowired
    Client client;

    @Value("classpath:/mapping/organisation.json")
    Resource organisationMapping;

    @Before
    public void setUp() throws Exception {
        String mapping = StreamUtils.copyToString(organisationMapping.getInputStream(), Charset.forName("UTF8"));
        this.organisationRepository.createIndex(mapping);
    }

    private void flush(String indexName) {
        this.client.admin().indices().prepareFlush(indexName).setWaitIfOngoing(true).execute().actionGet();
    }

    @After
    public void tearDown() throws Exception {
        this.organisationRepository.deleteIndex();
    }

    @Test
    public void existsOrganisationWithSameTypeInDistance_withSameOrganisationAndLocation_returnsTrue() throws Exception {
        // Arrange
        Organisation organisation = createOrganisation(OrganisationType.THW, new GeoPoint(49.0, 8.5));
        String indexName = this.elasticsearchConfiguration.getIndex();
        this.organisationRepository.save(Collections.singletonList(organisation));
        flush(indexName);

        // Act
        boolean exists = this.organisationRepository.existsOrganisationWithSameTypeInDistance(
            organisation,
            1L
        );

        // Assert
        assertThat(exists).isTrue();
    }

    @Test
    public void existsOrganisationWithSameTypeInDistance_withDifferentOrganisationAndSameLocation_returnsFalse() throws Exception {
        // Arrange
        Organisation createdOrganisation = createOrganisation(OrganisationType.THW, new GeoPoint(49.0, 8.5));
        String indexName = this.elasticsearchConfiguration.getIndex();
        this.organisationRepository.save(Collections.singletonList(createdOrganisation));
        flush(indexName);
        Organisation organisation = createOrganisation(OrganisationType.FF, new GeoPoint(49.0, 8.5));

        // Act
        boolean exists = this.organisationRepository.existsOrganisationWithSameTypeInDistance(
            organisation,
            1L
        );

        // Assert
        assertThat(exists).isFalse();
    }

    @Test
    public void existsOrganisationWithSameTypeInDistance_withSameOrganisationAndDifferentLocation_returnsFalse() throws Exception {
        // Arrange
        Organisation createdOrganisation = createOrganisation(OrganisationType.THW, new GeoPoint(49.0, 8.5));
        String indexName = this.elasticsearchConfiguration.getIndex();
        this.organisationRepository.save(Collections.singletonList(createdOrganisation));
        flush(indexName);
        Organisation organisation = createOrganisation(OrganisationType.THW, new GeoPoint(49.0, 8.6));

        // Act
        boolean exists = this.organisationRepository.existsOrganisationWithSameTypeInDistance(
            organisation,
            1L
        );

        // Assert
        assertThat(exists).isFalse();
    }

    private Organisation createOrganisation(OrganisationType organisationType, GeoPoint position) {
        return new Organisation.Builder()
            .setId(new OrganisationId().getValue())
            .setName("Test Organisation")
            .setOrganisationType(organisationType)
            .setDefaultAddress(new Address.Builder()
                .setLocation(position)
                .build())
            .build();
    }

}