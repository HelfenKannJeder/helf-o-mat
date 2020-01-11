package de.helfenkannjeder.helfomat.infrastructure.typo3;

import de.helfenkannjeder.helfomat.core.IndexManager;
import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.picture.DownloadFailedException;
import de.helfenkannjeder.helfomat.core.picture.PictureId;
import de.helfenkannjeder.helfomat.core.picture.PictureRepository;
import de.helfenkannjeder.helfomat.infrastructure.typo3.domain.TOrganisation;
import de.helfenkannjeder.helfomat.infrastructure.typo3.domain.TOrganisationType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class Typo3OrganisationProcessorTest {

    private Typo3OrganisationProcessor typo3OrganisationProcessor;

    @Mock
    private PictureRepository pictureRepository;

    @Mock
    private IndexManager indexManager;

    @Before
    public void setUp() {
        this.typo3OrganisationProcessor = new Typo3OrganisationProcessor(pictureRepository, indexManager);
    }

    @Test
    public void organisationsWithoutTypeAreIgnored() {
        TOrganisation tOrganisation = new TOrganisation();
        Organisation processedOrganisation = typo3OrganisationProcessor.process(tOrganisation);

        assertThat(processedOrganisation).isNull();
    }

    @Test
    public void organisationsOfTypeAktivbueroAreIgnored() {
        TOrganisation tOrganisation = new TOrganisation();
        TOrganisationType tOrganisationType = new TOrganisationType();
        tOrganisationType.setName("Aktivbüro");
        tOrganisation.setOrganisationtype(tOrganisationType);
        Organisation processedOrganisation = typo3OrganisationProcessor.process(tOrganisation);

        assertThat(processedOrganisation).isNull();
    }

    @Test
    public void toPicture_withSameUrls_ensureThatUuidIsStatic() throws DownloadFailedException {
        // Arrange

        // Act
        typo3OrganisationProcessor.toPicture("dummyPicture.jpg");
        typo3OrganisationProcessor.toPicture("dummyPicture.jpg");

        // Assert
        List<PictureId> values = getPictureIdsOfLastInsert();
        assertThat(values.get(0)).isEqualTo(values.get(1));
    }

    @Test
    public void toPicture_withDifferentUrls_ensureThatUuidIsStatic() throws DownloadFailedException {
        // Arrange

        // Act
        typo3OrganisationProcessor.toPicture("dummyPicture.jpg");
        typo3OrganisationProcessor.toPicture("dummyPicture2.jpg");

        // Assert
        List<PictureId> values = getPictureIdsOfLastInsert();
        assertThat(values.get(0)).isNotEqualTo(values.get(1));
    }

    private List<PictureId> getPictureIdsOfLastInsert() throws DownloadFailedException {
        ArgumentCaptor<PictureId> pictureIdArgumentCaptor = ArgumentCaptor.forClass(PictureId.class);
        verify(pictureRepository, atLeastOnce()).savePicture(anyString(), eq(null), pictureIdArgumentCaptor.capture());

        return pictureIdArgumentCaptor.getAllValues();
    }

}