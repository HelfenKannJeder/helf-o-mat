package de.helfenkannjeder.helfomat.infrastructure.typo3;

import de.helfenkannjeder.helfomat.core.IndexManager;
import de.helfenkannjeder.helfomat.core.organization.Organization;
import de.helfenkannjeder.helfomat.core.picture.DownloadFailedException;
import de.helfenkannjeder.helfomat.core.picture.PictureId;
import de.helfenkannjeder.helfomat.core.picture.PictureRepository;
import de.helfenkannjeder.helfomat.infrastructure.typo3.domain.TOrganization;
import de.helfenkannjeder.helfomat.infrastructure.typo3.domain.TOrganizationType;
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
public class Typo3OrganizationProcessorTest {

    private Typo3OrganizationProcessor typo3OrganizationProcessor;

    @Mock
    private PictureRepository pictureRepository;

    @Mock
    private IndexManager indexManager;

    @Before
    public void setUp() {
        this.typo3OrganizationProcessor = new Typo3OrganizationProcessor(pictureRepository, indexManager);
    }

    @Test
    public void organizationsWithoutTypeAreIgnored() {
        TOrganization tOrganization = new TOrganization();
        Organization processedOrganization = typo3OrganizationProcessor.process(tOrganization);

        assertThat(processedOrganization).isNull();
    }

    @Test
    public void organizationsOfTypeAktivbueroAreIgnored() {
        TOrganization tOrganization = new TOrganization();
        TOrganizationType tOrganizationType = new TOrganizationType();
        tOrganizationType.setName("Aktivbüro");
        tOrganization.setOrganizationtype(tOrganizationType);
        Organization processedOrganization = typo3OrganizationProcessor.process(tOrganization);

        assertThat(processedOrganization).isNull();
    }

    @Test
    public void toPicture_withSameUrls_ensureThatUuidIsStatic() throws DownloadFailedException {
        // Arrange

        // Act
        typo3OrganizationProcessor.toPicture("dummyPicture.jpg");
        typo3OrganizationProcessor.toPicture("dummyPicture.jpg");

        // Assert
        List<PictureId> values = getPictureIdsOfLastInsert();
        assertThat(values.get(0)).isEqualTo(values.get(1));
    }

    @Test
    public void toPicture_withDifferentUrls_ensureThatUuidIsStatic() throws DownloadFailedException {
        // Arrange

        // Act
        typo3OrganizationProcessor.toPicture("dummyPicture.jpg");
        typo3OrganizationProcessor.toPicture("dummyPicture2.jpg");

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