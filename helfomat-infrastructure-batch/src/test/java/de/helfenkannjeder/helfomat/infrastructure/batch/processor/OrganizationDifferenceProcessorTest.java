package de.helfenkannjeder.helfomat.infrastructure.batch.processor;

import de.helfenkannjeder.helfomat.core.organization.Organization;
import de.helfenkannjeder.helfomat.core.organization.OrganizationId;
import de.helfenkannjeder.helfomat.core.organization.OrganizationRepository;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEditNameEvent;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.util.Pair;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * @author Valentin Zickner
 */
@RunWith(MockitoJUnitRunner.class)
public class OrganizationDifferenceProcessorTest {

    @Mock
    private OrganizationRepository organizationRepository;

    @Mock
    private OrganizationRepository generalOrganizationRepository;

    private OrganizationDifferenceProcessor organizationDifferenceProcessor;

    @Before
    public void setUp() {
        this.organizationDifferenceProcessor = new OrganizationDifferenceProcessor(this.organizationRepository, generalOrganizationRepository);
    }

    @Test
    public void process_withExistingOrganization_returnsNameChangedEvent() {
        // Arrange
        Organization organization1 = new Organization.Builder()
            .setName("New Organization Name")
            .setDescription("Same Description")
            .build();
        Organization organization2 = new Organization.Builder()
            .setName("Old Organization Name")
            .setDescription("Same Description")
            .build();
        when(organizationRepository.findOrganizationWithSameTypeInDistance(eq(organization1), anyLong())).thenReturn(organization2);

        // Act
        Pair<Organization, Stream<OrganizationEvent>> organizationDifferenceResult = this.organizationDifferenceProcessor.process(organization1);

        // Assert
        assertThat(organizationDifferenceResult).isNotNull();
        assertThat(organizationDifferenceResult.getFirst()).isEqualTo(organization1);
        Stream<OrganizationEvent> organizationEventStream = organizationDifferenceResult.getSecond();
        assertThat(organizationEventStream).isNotNull();
        List<OrganizationEvent> organizationEvents = organizationEventStream.collect(Collectors.toList());
        assertThat(organizationEvents).hasSize(1);
        OrganizationEvent actual = organizationEvents.get(0);
        assertThat(actual)
            .isNotNull()
            .isInstanceOf(OrganizationEditNameEvent.class);
        OrganizationEditNameEvent organizationEditNameEvent = (OrganizationEditNameEvent) actual;
        assertThat(organizationEditNameEvent.getName()).isEqualTo("New Organization Name");
    }

    @Test
    public void process_withOrganizationFromGeneralRepository_returnsNoEventsAndExistingUid() {
        // Arrange
        OrganizationId resultOrganizationId = new OrganizationId();
        Organization organization1 = new Organization.Builder()
            .setName("New Organization Name")
            .setDescription("Same Description")
            .build();
        Organization organization2 = new Organization.Builder()
            .setId(resultOrganizationId)
            .setName("Old Organization Name")
            .setDescription("Same Description")
            .build();
        when(generalOrganizationRepository.findOrganizationWithSameTypeInDistance(eq(organization1), anyLong())).thenReturn(organization2);

        // Act
        Pair<Organization, Stream<OrganizationEvent>> organizationDifferenceResult = this.organizationDifferenceProcessor.process(organization1);

        // Assert
        assertThat(organizationDifferenceResult).isNotNull();
        Organization organization = organizationDifferenceResult.getFirst();
        assertThat(organization).isNotNull();
        assertThat(organization.getId()).isEqualTo(resultOrganizationId);
        assertThat(organization.getName()).isEqualTo("New Organization Name");
        assertThat(organizationDifferenceResult.getSecond())
            .isNotNull();
        assertThat(organizationDifferenceResult.getSecond().collect(Collectors.toList()))
            .hasSize(0);
    }

}