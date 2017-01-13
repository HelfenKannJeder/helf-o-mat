package de.helfenkannjeder.helfomat.processor;

import de.helfenkannjeder.helfomat.domain.Organisation;
import de.helfenkannjeder.helfomat.repository.OrganisationRepository;
import de.helfenkannjeder.helfomat.support.OrganisationMother;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DuplicateOrganisationFilterProcessorTest {

    private OrganisationRepository organisationRepository = mock(OrganisationRepository.class);
    private DuplicateOrganisationFilterProcessor duplicateOrganisationFilterProcessor = new DuplicateOrganisationFilterProcessor(organisationRepository);

    @Test
    public void anExistingOrganisationIsNotReturned() throws Exception {
        Organisation candidateOrganisation = OrganisationMother.anyOrganisation().build();
        when(organisationRepository.existsOrganisationWithSameTypeInDistance(any(), any())).thenReturn(true);

        Organisation processedOrganisation = duplicateOrganisationFilterProcessor.process(candidateOrganisation);

        assertThat(processedOrganisation, CoreMatchers.nullValue());
    }
}