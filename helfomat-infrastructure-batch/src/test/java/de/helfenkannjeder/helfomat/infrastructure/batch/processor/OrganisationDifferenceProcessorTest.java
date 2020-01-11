package de.helfenkannjeder.helfomat.infrastructure.batch.processor;

import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationRepository;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEditNameEvent;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEvent;
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
public class OrganisationDifferenceProcessorTest {

    @Mock
    private OrganisationRepository organisationRepository;

    @Mock
    private OrganisationRepository generalOrganisationRepository;

    private OrganisationDifferenceProcessor organisationDifferenceProcessor;

    @Before
    public void setUp() {
        this.organisationDifferenceProcessor = new OrganisationDifferenceProcessor(this.organisationRepository, generalOrganisationRepository);
    }

    @Test
    public void process_withExistingOrganisation_returnsNameChangedEvent() {
        // Arrange
        Organisation organisation1 = new Organisation.Builder()
            .setName("New Organisation Name")
            .setDescription("Same Description")
            .build();
        Organisation organisation2 = new Organisation.Builder()
            .setName("Old Organisation Name")
            .setDescription("Same Description")
            .build();
        when(organisationRepository.findOrganisationWithSameTypeInDistance(eq(organisation1), anyLong())).thenReturn(organisation2);

        // Act
        Pair<Organisation, Stream<OrganisationEvent>> organisationDifferenceResult = this.organisationDifferenceProcessor.process(organisation1);

        // Assert
        assertThat(organisationDifferenceResult).isNotNull();
        assertThat(organisationDifferenceResult.getFirst()).isEqualTo(organisation1);
        Stream<OrganisationEvent> organisationEventStream = organisationDifferenceResult.getSecond();
        assertThat(organisationEventStream).isNotNull();
        List<OrganisationEvent> organisationEvents = organisationEventStream.collect(Collectors.toList());
        assertThat(organisationEvents).hasSize(1);
        OrganisationEvent actual = organisationEvents.get(0);
        assertThat(actual)
            .isNotNull()
            .isInstanceOf(OrganisationEditNameEvent.class);
        OrganisationEditNameEvent organisationEditNameEvent = (OrganisationEditNameEvent) actual;
        assertThat(organisationEditNameEvent.getName()).isEqualTo("New Organisation Name");
    }

    @Test
    public void process_withOrganisationFromGeneralRepository_returnsNoEventsAndExistingUid() {
        // Arrange
        OrganisationId resultOrganisationId = new OrganisationId();
        Organisation organisation1 = new Organisation.Builder()
            .setName("New Organisation Name")
            .setDescription("Same Description")
            .build();
        Organisation organisation2 = new Organisation.Builder()
            .setId(resultOrganisationId)
            .setName("Old Organisation Name")
            .setDescription("Same Description")
            .build();
        when(generalOrganisationRepository.findOrganisationWithSameTypeInDistance(eq(organisation1), anyLong())).thenReturn(organisation2);

        // Act
        Pair<Organisation, Stream<OrganisationEvent>> organisationDifferenceResult = this.organisationDifferenceProcessor.process(organisation1);

        // Assert
        assertThat(organisationDifferenceResult).isNotNull();
        Organisation organisation = organisationDifferenceResult.getFirst();
        assertThat(organisation).isNotNull();
        assertThat(organisation.getId()).isEqualTo(resultOrganisationId);
        assertThat(organisation.getName()).isEqualTo("New Organisation Name");
        assertThat(organisationDifferenceResult.getSecond())
            .isNotNull();
        assertThat(organisationDifferenceResult.getSecond().collect(Collectors.toList()))
            .hasSize(0);
    }

}