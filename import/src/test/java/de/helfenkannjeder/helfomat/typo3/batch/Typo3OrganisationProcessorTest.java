package de.helfenkannjeder.helfomat.typo3.batch;

import de.helfenkannjeder.helfomat.domain.Organisation;
import de.helfenkannjeder.helfomat.typo3.domain.TOrganisation;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class Typo3OrganisationProcessorTest {

    private Typo3OrganisationProcessor typo3OrganisationProcessor = new Typo3OrganisationProcessor(null);

    @Test
    public void organisationsWithoutTypeAreIgnored() throws Exception {
        TOrganisation tOrganisation = new TOrganisation();
        Organisation processedOrganisation = typo3OrganisationProcessor.process(tOrganisation);

        assertThat(processedOrganisation, nullValue());
    }
}