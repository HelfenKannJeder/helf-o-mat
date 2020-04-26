package de.helfenkannjeder.helfomat.infrastructure.typo3;

import de.helfenkannjeder.helfomat.core.IndexManager;
import de.helfenkannjeder.helfomat.core.organization.Organization;
import de.helfenkannjeder.helfomat.core.picture.DownloadFailedException;
import de.helfenkannjeder.helfomat.core.picture.PictureId;
import de.helfenkannjeder.helfomat.core.picture.PictureStorageService;
import de.helfenkannjeder.helfomat.infrastructure.typo3.domain.TOrganization;
import de.helfenkannjeder.helfomat.infrastructure.typo3.domain.TOrganizationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class Typo3OrganizationProcessorTest {

    private Typo3OrganizationProcessor typo3OrganizationProcessor;

    @Mock
    private PictureStorageService pictureStorageService;

    @Mock
    private IndexManager indexManager;

    @BeforeEach
    void setUp() {
        this.typo3OrganizationProcessor = new Typo3OrganizationProcessor(pictureStorageService);
    }

    @Test
    void organizationsWithoutTypeAreIgnored() {
        TOrganization tOrganization = new TOrganization();
        Organization processedOrganization = typo3OrganizationProcessor.process(tOrganization);

        assertThat(processedOrganization).isNull();
    }

    @Test
    void organizationsOfTypeAktivbueroAreIgnored() {
        TOrganization tOrganization = new TOrganization();
        TOrganizationType tOrganizationType = new TOrganizationType();
        tOrganizationType.setName("Aktivbüro");
        tOrganization.setOrganizationtype(tOrganizationType);
        Organization processedOrganization = typo3OrganizationProcessor.process(tOrganization);

        assertThat(processedOrganization).isNull();
    }

    @Test
    void toPicture_withSameUrls_ensureThatUuidIsStatic() throws DownloadFailedException {
        // Arrange

        // Act
        typo3OrganizationProcessor.toPicture("dummyPicture.jpg");
        typo3OrganizationProcessor.toPicture("dummyPicture.jpg");

        // Assert
        List<PictureId> values = getPictureIdsOfLastInsert();
        assertThat(values.get(0)).isEqualTo(values.get(1));
    }

    @Test
    void toPicture_withDifferentUrls_ensureThatUuidIsStatic() throws DownloadFailedException {
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
        verify(pictureStorageService, atLeastOnce()).savePicture(anyString(), pictureIdArgumentCaptor.capture());

        return pictureIdArgumentCaptor.getAllValues();
    }

}