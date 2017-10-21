package de.helfenkannjeder.helfomat.infrastructure.typo3;

import de.helfekannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.infrastructure.typo3.domain.TOrganisation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * @author Valentin Zickner
 */
@RunWith(MockitoJUnitRunner.class)
public class Typo3OrganisationReaderTest {

    @Mock
    private Typo3OrganisationItemReader typo3OrganisationItemReader;

    @Mock
    private Typo3OrganisationProcessor typo3OrganisationProcessor;

    private Typo3OrganisationReader typo3OrganisationReader;

    @Before
    public void setUp() throws Exception {
        this.typo3OrganisationReader = new Typo3OrganisationReader(this.typo3OrganisationItemReader, this.typo3OrganisationProcessor);
    }

    @Test
    public void read_withFirstProcessIsNull_returnsNextItem() throws Exception {
        // Arrange
        TOrganisation tOrganisation1 = new TOrganisation();
        TOrganisation tOrganisation2 = new TOrganisation();
        Organisation organisation = new Organisation.Builder().build();
        when(typo3OrganisationItemReader.read()).thenReturn(tOrganisation1, tOrganisation2);
        when(typo3OrganisationProcessor.process(tOrganisation1)).thenReturn(null);
        when(typo3OrganisationProcessor.process(tOrganisation2)).thenReturn(organisation);

        // Act
        Organisation read = this.typo3OrganisationReader.read();

        // Assert
        assertThat(read)
            .isNotNull()
            .isEqualTo(organisation);
    }

    @Test
    public void read_withNullFromReader_returnsNull() throws Exception {
        // Arrange
        when(typo3OrganisationItemReader.read()).thenReturn(null);

        // Act
        Organisation read = this.typo3OrganisationReader.read();

        // Assert
        assertThat(read).isNull();
    }

}