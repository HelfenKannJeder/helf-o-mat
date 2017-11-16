package de.helfenkannjeder.helfomat.infrastructure.typo3;

import de.helfenkannjeder.helfomat.core.organisation.Address;
import de.helfenkannjeder.helfomat.core.organisation.ContactPerson;
import de.helfenkannjeder.helfomat.core.organisation.Event;
import de.helfenkannjeder.helfomat.core.organisation.Group;
import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import org.assertj.core.data.Offset;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Valentin Zickner
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class Typo3OrganisationReaderIntegrationTest {

    @Autowired
    Typo3OrganisationReader typo3OrganisationReader;

    @Test
    @DirtiesContext
    public void read_withThwKarlsruhe_returnsBasicInformation() throws Exception {
        // Arrange

        // Act
        Organisation organisation = readByName("THW Karlsruhe");

        // Assert
        assertThat(organisation).isNotNull();
        assertThat(organisation.getDescription()).startsWith("Wir sind die Hilfsorganisation des Bundes und unterstützen die für die Gefahrenabwehr");
        assertThat(organisation.getWebsite()).isEqualTo("http://www.thw-karlsruhe.de");

        Address defaultAddress = organisation.getDefaultAddress();
        assertThat(defaultAddress).isNotNull();
        assertThat(defaultAddress.getLocation()).isNotNull();
        assertThat(defaultAddress.getLocation().getLat()).isEqualTo(49.03766, Offset.offset(0.0001));
        assertThat(defaultAddress.getLocation().getLon()).isEqualTo(8.352747, Offset.offset(0.0001));
        assertThat(defaultAddress.getStreet()).isEqualTo("Grünhutstr. 9");
        assertThat(defaultAddress.getZipcode()).isEqualTo("76187");
        assertThat(defaultAddress.getCity()).isEqualTo("Karlsruhe");
        assertThat(defaultAddress.getTelephone()).isEqualTo("0123 456789");
        assertThat(defaultAddress.getWebsite()).isNullOrEmpty();

        assertThat(organisation.getQuestions()).isNullOrEmpty();

        List<ContactPerson> contactPersons = organisation.getContactPersons();
        assertThat(contactPersons).hasSize(1);
        ContactPerson contactPerson = contactPersons.get(0);
        assertThat(contactPerson.getFirstname()).isEqualTo("David");
        assertThat(contactPerson.getLastname()).isEqualTo("Müller1");
        assertThat(contactPerson.getRank()).isEqualTo("Beauftragter für Öffentlichkeitsarbeit");
        assertThat(contactPerson.getTelephone()).isEmpty();
        assertThat(contactPerson.getMail()).isEqualTo("no-spam@helfenkannjeder.de");

        List<Event> events = organisation.getEvents();
        assertThat(events)
            .isNotNull()
            .hasSize(3);

        Event event = events.get(0);
        assertThat(event.getDay()).isEqualTo(DayOfWeek.TUESDAY);
        assertThat(event.getStart()).isEqualTo(LocalTime.of(19, 30));
        assertThat(event.getEnd()).isEqualTo(LocalTime.of(22, 30));
        assertThat(event.getNote()).isEqualTo("unregelmäßig (Dienstplan siehe Homepage)");
        assertThat(event.getGroups()
            .stream()
            .map(Group::getName)
            .collect(Collectors.toList())
        )
            .hasSize(6)
            .contains("1. Bergungsgruppe", "Zugtrupp", "OV-Stab")
            .doesNotContain("Bambini-/Mini-Jugendgruppe");
    }

    @Test
    @DirtiesContext
    public void read_withAsbkarlsruhe_returnsBasicInformation() throws Exception {
        // Arrange

        // Act
        Organisation organisation = readByName("ASB Karlsruhe");

        // Assert
        assertThat(organisation).isNotNull();
    }

    private Organisation readByName(String name) throws Exception {
        Organisation read;
        do {
            read = typo3OrganisationReader.read();
        } while (read != null && !read.getName().equals(name));
        return read;
    }

}