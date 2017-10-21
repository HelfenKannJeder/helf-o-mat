package de.helfenkannjeder.helfomat.infrastructure.batch.processor;

import de.helfenkannjeder.helfomat.core.IndexManager;
import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DuplicateOrganisationFilterProcessorTest {

    @Mock
    private OrganisationRepository organisationRepository;

    @Mock
    private IndexManager indexManager;

    private DuplicateOrganisationFilterProcessor duplicateOrganisationFilterProcessor;

    @Before
    public void setUp() throws Exception {
        this.duplicateOrganisationFilterProcessor = new DuplicateOrganisationFilterProcessor(organisationRepository, indexManager);
    }

    @Test
    public void anExistingOrganisationIsNotReturned() throws Exception {
        Organisation candidateOrganisation = new Organisation.Builder()
            .setId("1")
            .setType("anyType")
            .build();
        String indexName = "my-index-name";
        when(indexManager.getCurrentIndex()).thenReturn(indexName);
        when(organisationRepository.existsOrganisationWithSameTypeInDistance(eq(indexName), any(), any())).thenReturn(true);

        Organisation processedOrganisation = duplicateOrganisationFilterProcessor.process(candidateOrganisation);

        assertThat(processedOrganisation).isNull();
    }
}