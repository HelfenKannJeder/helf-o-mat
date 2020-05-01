package de.helfenkannjeder.helfomat.infrastructure.typo3

import de.helfenkannjeder.helfomat.core.picture.DownloadFailedException
import de.helfenkannjeder.helfomat.core.picture.PictureId
import de.helfenkannjeder.helfomat.core.picture.PictureStorageService
import de.helfenkannjeder.helfomat.infrastructure.typo3.domain.TOrganization
import de.helfenkannjeder.helfomat.infrastructure.typo3.domain.TOrganizationType
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class Typo3OrganizationProcessorTest {
    private var typo3OrganizationProcessor: Typo3OrganizationProcessor? = null

    @Mock
    private lateinit var pictureStorageService: PictureStorageService

    @BeforeEach
    fun setUp() {
        typo3OrganizationProcessor = Typo3OrganizationProcessor(pictureStorageService)
    }

    @Test
    fun organizationsWithoutTypeAreIgnored() {
        val tOrganization = TOrganization()
        val processedOrganization = typo3OrganizationProcessor!!.process(tOrganization)
        Assertions.assertThat(processedOrganization).isNull()
    }

    @Test
    fun organizationsOfTypeAktivbueroAreIgnored() {
        val tOrganization = TOrganization()
        val tOrganizationType = TOrganizationType()
        tOrganizationType.name = "Aktivbüro"
        tOrganization.organizationtype = tOrganizationType
        val processedOrganization = typo3OrganizationProcessor!!.process(tOrganization)
        Assertions.assertThat(processedOrganization).isNull()
    }

    @Test
    @Throws(DownloadFailedException::class)
    fun toPicture_withSameUrls_ensureThatUuidIsStatic() {
        // Arrange

        // Act
        typo3OrganizationProcessor!!.toPicture("dummyPicture.jpg")
        typo3OrganizationProcessor!!.toPicture("dummyPicture.jpg")

        // Assert
        val values = pictureIdsOfLastInsert()
        Assertions.assertThat(values[0]).isEqualTo(values[1])
    }

    @Test
    @Throws(DownloadFailedException::class)
    fun toPicture_withDifferentUrls_ensureThatUuidIsStatic() {
        // Arrange

        // Act
        typo3OrganizationProcessor!!.toPicture("dummyPicture.jpg")
        typo3OrganizationProcessor!!.toPicture("dummyPicture2.jpg")

        // Assert
        val values = pictureIdsOfLastInsert()
        Assertions.assertThat(values[0]).isNotEqualTo(values[1])
    }

    @Throws(DownloadFailedException::class)
    private fun pictureIdsOfLastInsert(): List<PictureId> {
        val pictureIdArgumentCaptor = ArgumentCaptor.forClass(PictureId::class.java)
        Mockito.verify(pictureStorageService, Mockito.atLeastOnce()).savePicture(ArgumentMatchers.anyString(), pictureIdArgumentCaptor.capture())
        return pictureIdArgumentCaptor.allValues
    }
}