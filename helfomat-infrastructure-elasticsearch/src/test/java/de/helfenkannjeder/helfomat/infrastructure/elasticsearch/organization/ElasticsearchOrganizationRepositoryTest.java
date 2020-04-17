package de.helfenkannjeder.helfomat.infrastructure.elasticsearch.organization;

import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint;
import de.helfenkannjeder.helfomat.core.organization.Address;
import de.helfenkannjeder.helfomat.core.organization.Organization;
import de.helfenkannjeder.helfomat.core.organization.OrganizationId;
import de.helfenkannjeder.helfomat.core.organization.OrganizationType;
import de.helfenkannjeder.helfomat.infrastructure.elasticsearch.ElasticsearchConfiguration;
import org.assertj.core.api.Assertions;
import org.elasticsearch.client.Client;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
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

/**
 * @author Valentin Zickner
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Ignore("Not working anymore since there is no embedded elasticsearch")
public class ElasticsearchOrganizationRepositoryTest {

    @Autowired
    ElasticsearchConfiguration elasticsearchConfiguration;

    @Autowired
    ElasticsearchOrganizationRepository organizationRepository;

    @Autowired
    Client client;

    @Value("classpath:/mapping/organization.json")
    Resource organizationMapping;

    @Before
    public void setUp() throws Exception {
        String mapping = StreamUtils.copyToString(organizationMapping.getInputStream(), Charset.forName("UTF8"));
        this.organizationRepository.createIndex(mapping);
    }

    private void flush(String indexName) {
        this.client.admin().indices().prepareFlush(indexName).setWaitIfOngoing(true).execute().actionGet();
    }

    @After
    public void tearDown() {
        this.organizationRepository.deleteIndex();
    }

    @Test
    public void existsOrganizationWithSameTypeInDistance_withSameOrganizationAndLocation_returnsTrue() {
        // Arrange
        Organization organization = createOrganization(OrganizationType.THW, new GeoPoint(49.0, 8.5));
        String indexName = this.elasticsearchConfiguration.getIndex();
        this.organizationRepository.save(Collections.singletonList(organization));
        flush(indexName);

        // Act
        boolean exists = this.organizationRepository.existsOrganizationWithSameTypeInDistance(
            organization,
            1L
        );

        // Assert
        Assertions.assertThat(exists).isTrue();
    }

    @Test
    public void existsOrganizationWithSameTypeInDistance_withDifferentOrganizationAndSameLocation_returnsFalse() {
        // Arrange
        Organization createdOrganization = createOrganization(OrganizationType.THW, new GeoPoint(49.0, 8.5));
        String indexName = this.elasticsearchConfiguration.getIndex();
        this.organizationRepository.save(Collections.singletonList(createdOrganization));
        flush(indexName);
        Organization organization = createOrganization(OrganizationType.FF, new GeoPoint(49.0, 8.5));

        // Act
        boolean exists = this.organizationRepository.existsOrganizationWithSameTypeInDistance(
            organization,
            1L
        );

        // Assert
        Assertions.assertThat(exists).isFalse();
    }

    @Test
    public void existsOrganizationWithSameTypeInDistance_withSameOrganizationAndDifferentLocation_returnsFalse() {
        // Arrange
        Organization createdOrganization = createOrganization(OrganizationType.THW, new GeoPoint(49.0, 8.5));
        String indexName = this.elasticsearchConfiguration.getIndex();
        this.organizationRepository.save(Collections.singletonList(createdOrganization));
        flush(indexName);
        Organization organization = createOrganization(OrganizationType.THW, new GeoPoint(49.0, 8.6));

        // Act
        boolean exists = this.organizationRepository.existsOrganizationWithSameTypeInDistance(
            organization,
            1L
        );

        // Assert
        Assertions.assertThat(exists).isFalse();
    }

    private Organization createOrganization(OrganizationType organizationType, GeoPoint position) {
        return new Organization.Builder()
            .setId(new OrganizationId())
            .setName("Test Organization")
            .setOrganizationType(organizationType)
            .setDefaultAddress(new Address.Builder()
                .setLocation(position)
                .build())
            .build();
    }

}