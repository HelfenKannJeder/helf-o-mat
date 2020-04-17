package de.helfenkannjeder.helfomat.infrastructure.typo3;

import de.helfenkannjeder.helfomat.core.organization.Address;
import de.helfenkannjeder.helfomat.core.organization.AttendanceTime;
import de.helfenkannjeder.helfomat.core.organization.ContactPerson;
import de.helfenkannjeder.helfomat.core.organization.Group;
import de.helfenkannjeder.helfomat.core.organization.Organization;
import de.helfenkannjeder.helfomat.core.organization.Volunteer;
import org.assertj.core.data.Offset;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.test.JobScopeTestExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
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
@TestExecutionListeners({
    DependencyInjectionTestExecutionListener.class,
    JobScopeTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class
})
@Transactional(transactionManager = "legacyTransactionManager")
public class Typo3OrganizationReaderIntegrationTest {

    @Autowired
    Typo3OrganizationReader typo3OrganizationReader;

    @Test
    @DirtiesContext
    public void read_withThwKarlsruhe_returnsBasicInformation() {
        // Arrange

        // Act
        Organization organization = readByName("THW Karlsruhe");

        // Assert
        assertThat(organization).isNotNull();
        assertThat(organization.getDescription()).startsWith("Wir sind die Hilfsorganisation des Bundes und unterstützen die für die Gefahrenabwehr");
        assertThat(organization.getWebsite()).isEqualTo("http://www.thw-karlsruhe.de");

        Address defaultAddress = organization.getDefaultAddress();
        assertThat(defaultAddress).isNotNull();
        assertThat(defaultAddress.getLocation()).isNotNull();
        assertThat(defaultAddress.getLocation().getLat()).isEqualTo(49.03766, Offset.offset(0.0001));
        assertThat(defaultAddress.getLocation().getLon()).isEqualTo(8.352747, Offset.offset(0.0001));
        assertThat(defaultAddress.getStreet()).isEqualTo("Grünhutstr. 9");
        assertThat(defaultAddress.getZipcode()).isEqualTo("76187");
        assertThat(defaultAddress.getCity()).isEqualTo("Karlsruhe");
        assertThat(defaultAddress.getTelephone()).isEqualTo("0123 456789");
        assertThat(defaultAddress.getWebsite()).isNullOrEmpty();

        assertThat(organization.getQuestionAnswers()).isNullOrEmpty();

        List<ContactPerson> contactPersons = organization.getContactPersons();
        assertThat(contactPersons).hasSize(1);
        ContactPerson contactPerson = contactPersons.get(0);
        assertThat(contactPerson.getFirstname()).isEqualTo("David");
        assertThat(contactPerson.getLastname()).isEqualTo("Müller1");
        assertThat(contactPerson.getRank()).isEqualTo("Beauftragter für Öffentlichkeitsarbeit");
        assertThat(contactPerson.getTelephone()).isEmpty();
        assertThat(contactPerson.getMail()).isEqualTo("no-spam@helfenkannjeder.de");

        List<AttendanceTime> attendanceTimes = organization.getAttendanceTimes();
        assertThat(attendanceTimes)
            .isNotNull()
            .hasSize(3);

        AttendanceTime attendanceTime = attendanceTimes.get(0);
        assertThat(attendanceTime.getDay()).isEqualTo(DayOfWeek.TUESDAY);
        assertThat(attendanceTime.getStart()).isEqualTo(LocalTime.of(19, 30));
        assertThat(attendanceTime.getEnd()).isEqualTo(LocalTime.of(22, 30));
        assertThat(attendanceTime.getNote()).isEqualTo("unregelmäßig (Dienstplan siehe Homepage)");
        assertThat(attendanceTime.getGroups()
            .stream()
            .map(Group::getName)
            .collect(Collectors.toList())
        )
            .hasSize(6)
            .contains("1. Bergungsgruppe", "Zugtrupp", "OV-Stab")
            .doesNotContain("Bambini-/Mini-Jugendgruppe");

        List<Group> groups = organization.getGroups();
        assertThat(groups).isNotNull();
        assertThat(groups
            .stream()
            .map(Group::getName)
            .collect(Collectors.toList()))
            .hasSize(10)
            .containsExactly(
                "OV-Stab",
                "Jugendgruppe",
                "Bambini-/Mini-Jugendgruppe",
                "Zugtrupp",
                "1. Bergungsgruppe",
                "Fachgruppe Räumen (FGr R, Typ A und B)",
                "Fachgruppe Beleuchtung (FGr Bel)",
                "Abstützen",
                "Feldkochherd",
                "Technische Hilfe auf Verkehrswegen (THV-Dienst)"
            );
        Group group = groups.get(1);
        assertThat(group.getWebsite()).isEqualTo("http://www.thw-jugend-karlsruhe.de");
        assertThat(group.getMinimumAge()).isEqualTo(10);
        assertThat(group.getMaximumAge()).isEqualTo(17);
        assertThat(group.getContactPersons())
            .isNotNull()
            .hasSize(1);
        ContactPerson groupContactPerson = group.getContactPersons().get(0);
        assertThat(groupContactPerson).isNotNull();
        assertThat(groupContactPerson.getFirstname()).isEqualTo("Christian");
        assertThat(groupContactPerson.getLastname()).isEqualTo("Müller8");

        assertThat(organization.getVolunteers()).isNotNull();
        assertThat(organization.getVolunteers()
            .stream()
            .map(Volunteer::getFirstname)
            .collect(Collectors.toList()))
            .hasSize(7)
            .containsExactly(
                "Carolin",
                "David",
                "Ilona",
                "Lisa",
                "Steffen",
                "Thilo",
                "Timo"
            );
        Volunteer volunteer = organization.getVolunteers().get(1);
        assertThat(volunteer).isNotNull();
        assertThat(volunteer.getFirstname()).isEqualTo("David");
        assertThat(volunteer.getLastname()).isEqualTo("Müller1");
        assertThat(volunteer.getMotivation()).startsWith("Viele Menschen, die mein Leben bereichern, hätte ich ohne das THW nie ");
    }

    @Test
    @DirtiesContext
    public void read_withAsbKarlsruhe_returnsBasicInformation() throws Exception {
        // Arrange

        // Act
        Organization organization = readByName("ASB Karlsruhe");

        // Assert
        assertThat(organization).isNotNull();
    }

    private Organization readByName(String name) {
        Organization read;
        do {
            read = typo3OrganizationReader.read();
        } while (read != null && !read.getName().equals(name));
        return read;
    }

}