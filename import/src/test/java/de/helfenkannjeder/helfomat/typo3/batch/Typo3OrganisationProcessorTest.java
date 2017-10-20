package de.helfenkannjeder.helfomat.typo3.batch;

import de.helfenkannjeder.helfomat.domain.Organisation;
import de.helfenkannjeder.helfomat.picture.PictureService;
import de.helfenkannjeder.helfomat.service.IndexManager;
import de.helfenkannjeder.helfomat.typo3.domain.TOrganisation;
import de.helfenkannjeder.helfomat.typo3.domain.TOrganisationType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class Typo3OrganisationProcessorTest {

    private Typo3OrganisationProcessor typo3OrganisationProcessor;

    @Mock
    private PictureService pictureService;

    @Mock
    private IndexManager indexManager;

    @Before
    public void setUp() throws Exception {
        this.typo3OrganisationProcessor = new Typo3OrganisationProcessor(pictureService, indexManager);
    }

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