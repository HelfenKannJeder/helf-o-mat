package de.helfenkannjeder.helfomat.infrastructure.typo3;

import de.helfenkannjeder.helfomat.core.IndexManager;
import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.picture.PictureRepository;
import de.helfenkannjeder.helfomat.infrastructure.typo3.domain.TOrganisation;
import de.helfenkannjeder.helfomat.infrastructure.typo3.domain.TOrganisationType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class Typo3OrganisationProcessorTest {

    private Typo3OrganisationProcessor typo3OrganisationProcessor;

    @Mock
    private PictureRepository pictureRepository;

    @Mock
    private IndexManager indexManager;

    @Before
    public void setUp() throws Exception {
        this.typo3OrganisationProcessor = new Typo3OrganisationProcessor(pictureRepository, indexManager);
    }

    @Test
    public void organisationsWithoutTypeAreIgnored() throws Exception {
        TOrganisation tOrganisation = new TOrganisation();
        Organisation processedOrganisation = typo3OrganisationProcessor.process(tOrganisation);

        assertThat(processedOrganisation).isNull();
    }

    @Test
    public void organisationsOfTypeAktivbueroAreIgnored() throws Exception {
        TOrganisation tOrganisation = new TOrganisation();
        TOrganisationType organisationtype = new TOrganisationType();
        organisationtype.setName("Aktivbüro");
        tOrganisation.setOrganisationtype(organisationtype);
        Organisation processedOrganisation = typo3OrganisationProcessor.process(tOrganisation);

        assertThat(processedOrganisation).isNull();
    }
}