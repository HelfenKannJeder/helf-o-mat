package de.helfenkannjeder.helfomat.infrastructure.batch.processor;

import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationRepository;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEditNameEvent;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

/**
 * @author Valentin Zickner
 */
@RunWith(MockitoJUnitRunner.class)
public class OrganisationDifferenceProcessorTest {

    @Mock
    private OrganisationRepository organisationRepository;

    private OrganisationDifferenceProcessor organisationDifferenceProcessor;

    @Before
    public void setUp() throws Exception {
        this.organisationDifferenceProcessor = new OrganisationDifferenceProcessor(this.organisationRepository);
    }

    @Test
    public void process_withExistingOrganisation_returnsNameChangedEvent() throws Exception {
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
        Stream<OrganisationEvent> organisationEventStream = this.organisationDifferenceProcessor.process(organisation1);

        // Assert
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
}