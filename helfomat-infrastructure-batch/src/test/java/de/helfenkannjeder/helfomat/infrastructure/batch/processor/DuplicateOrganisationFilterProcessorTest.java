package de.helfenkannjeder.helfomat.infrastructure.batch.processor;

import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationRepository;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DuplicateOrganisationFilterProcessorTest {

    @Mock
    private OrganisationRepository organisationRepository;

    private DuplicateOrganisationFilterProcessor duplicateOrganisationFilterProcessor;

    @Before
    public void setUp() {
        this.duplicateOrganisationFilterProcessor = new DuplicateOrganisationFilterProcessor(organisationRepository);
    }

    @Test
    public void anExistingOrganisationIsNotReturned() {
        Organisation candidateOrganisation = new Organisation.Builder()
            .setId(new OrganisationId())
            .setOrganisationType(OrganisationType.THW)
            .build();
        when(organisationRepository.existsOrganisationWithSameTypeInDistance(any(), any())).thenReturn(true);

        Organisation processedOrganisation = duplicateOrganisationFilterProcessor.process(candidateOrganisation);

        assertThat(processedOrganisation).isNull();
    }
}