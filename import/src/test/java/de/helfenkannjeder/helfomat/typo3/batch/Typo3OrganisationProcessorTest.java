package de.helfenkannjeder.helfomat.typo3.batch;

import de.helfenkannjeder.helfomat.domain.Organisation;
import de.helfenkannjeder.helfomat.typo3.domain.TOrganisation;
import de.helfenkannjeder.helfomat.typo3.domain.TOrganisationType;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class Typo3OrganisationProcessorTest {

    private Typo3OrganisationProcessor typo3OrganisationProcessor = new Typo3OrganisationProcessor();

    @Test
    public void organisationsWithoutTypeAreIgnored() throws Exception {
        TOrganisation tOrganisation = new TOrganisation();
        Organisation processedOrganisation = typo3OrganisationProcessor.process(tOrganisation);

        assertThat(processedOrganisation, nullValue());
    }

    @Test
    public void organisationsOfTypeAktivbueroAreIgnored() throws Exception {
        TOrganisation tOrganisation = new TOrganisation();
        TOrganisationType organisationtype = new TOrganisationType();
        organisationtype.setName("Aktivbüro");
        tOrganisation.setOrganisationtype(organisationtype);
        Organisation processedOrganisation = typo3OrganisationProcessor.process(tOrganisation);

        assertThat(processedOrganisation, nullValue());
    }
}