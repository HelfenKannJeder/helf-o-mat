package de.helfenkannjeder.helfomat.infrastructure.typo3

import de.helfenkannjeder.helfomat.core.organization.Group
import de.helfenkannjeder.helfomat.core.organization.Organization
import de.helfenkannjeder.helfomat.core.organization.Volunteer
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Offset
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.batch.test.JobScopeTestExecutionListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener
import org.springframework.test.context.support.DirtiesContextTestExecutionListener
import org.springframework.test.context.transaction.TransactionalTestExecutionListener
import org.springframework.transaction.annotation.Transactional
import java.time.DayOfWeek
import java.time.LocalTime
import java.util.function.Function

/**
 * @author Valentin Zickner
 */
@SpringBootTest
@ExtendWith(SpringExtension::class)
@TestExecutionListeners(DependencyInjectionTestExecutionListener::class, JobScopeTestExecutionListener::class, DirtiesContextTestExecutionListener::class, TransactionalTestExecutionListener::class)
@Transactional(transactionManager = "legacyTransactionManager")
open class Typo3OrganizationReaderIntegrationTest {

    @Autowired
    lateinit var typo3OrganizationReader: Typo3OrganizationReader

    @Test
    @DirtiesContext
    fun read_withThwKarlsruhe_returnsBasicInformation() {
        // Arrange

        // Act
        val organization = readByName("THW Karlsruhe")

        // Assert
        assertThat(organization).isNotNull
        assertThat(organization!!.description).startsWith("Wir sind die Hilfsorganisation des Bundes und unterstützen die für die Gefahrenabwehr")
        assertThat(organization.website).isEqualTo("http://www.thw-karlsruhe.de")
        val defaultAddress = organization.defaultAddress
        assertThat(defaultAddress).isNotNull
        assertThat(defaultAddress.location).isNotNull
        assertThat(defaultAddress.location.lat).isEqualTo(49.03766, Offset.offset(0.0001))
        assertThat(defaultAddress.location.lon).isEqualTo(8.352747, Offset.offset(0.0001))
        assertThat(defaultAddress.street).isEqualTo("Grünhutstr. 9")
        assertThat(defaultAddress.zipcode).isEqualTo("76187")
        assertThat(defaultAddress.city).isEqualTo("Karlsruhe")
        assertThat(defaultAddress.telephone).isEqualTo("0123 456789")
        assertThat(defaultAddress.website).isNullOrEmpty()
        assertThat(organization.questionAnswers).isNullOrEmpty()
        val contactPersons = organization.contactPersons
        assertThat(contactPersons).hasSize(1)
        val contactPerson = contactPersons[0]
        assertThat(contactPerson.firstname).isEqualTo("David")
        assertThat(contactPerson.lastname).isEqualTo("Müller1")
        assertThat(contactPerson.rank).isEqualTo("Beauftragter für Öffentlichkeitsarbeit")
        assertThat(contactPerson.telephone).isEmpty()
        assertThat(contactPerson.mail).isEqualTo("no-spam@helfenkannjeder.de")
        val attendanceTimes = organization.attendanceTimes
        assertThat(attendanceTimes)
            .isNotNull
            .hasSize(3)
        val attendanceTime = attendanceTimes[0]
        assertThat(attendanceTime.day).isEqualTo(DayOfWeek.TUESDAY)
        assertThat(attendanceTime.start).isEqualTo(LocalTime.of(19, 30))
        assertThat(attendanceTime.end).isEqualTo(LocalTime.of(22, 30))
        assertThat(attendanceTime.note).isEqualTo("unregelmäßig (Dienstplan siehe Homepage)")
        assertThat(attendanceTime.groups)
            .extracting(Function<Group, String> { it.name })
            .contains("1. Bergungsgruppe", "Zugtrupp", "OV-Stab")
        assertThat(attendanceTime.groups)
            .extracting(Function<Group, String> { it.name })
            .doesNotContain("Bambini-/Mini-Jugendgruppe")
        assertThat(organization.groups)
            .extracting(Function<Group, String> { it.name })
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
            )
        val group = organization.groups[1]
        assertThat(group.website).isEqualTo("http://www.thw-jugend-karlsruhe.de")
        assertThat(group.minimumAge).isEqualTo(10)
        assertThat(group.maximumAge).isEqualTo(17)
        assertThat(group.contactPersons)
            .isNotNull
            .hasSize(1)
        val groupContactPerson = group.contactPersons[0]
        assertThat(groupContactPerson).isNotNull
        assertThat(groupContactPerson.firstname).isEqualTo("Christian")
        assertThat(groupContactPerson.lastname).isEqualTo("Müller8")
        assertThat(organization.volunteers).isNotNull
        assertThat(organization.volunteers)
            .extracting(Function<Volunteer, String> { it.firstname })
            .containsExactly(
                "Carolin",
                "David",
                "Ilona",
                "Lisa",
                "Steffen",
                "Thilo",
                "Timo"
            )
        val volunteer = organization.volunteers[1]
        assertThat(volunteer).isNotNull
        assertThat(volunteer.firstname).isEqualTo("David")
        assertThat(volunteer.lastname).isEqualTo("Müller1")
        assertThat(volunteer.motivation).startsWith("Viele Menschen, die mein Leben bereichern, hätte ich ohne das THW nie ")
    }

    @Test
    @DirtiesContext
    @Throws(Exception::class)
    fun read_withAsbKarlsruhe_returnsBasicInformation() {
        // Arrange

        // Act
        val organization = readByName("ASB Karlsruhe")

        // Assert
        assertThat(organization).isNotNull
    }

    private fun readByName(name: String): Organization? {
        var read: Organization?
        do {
            read = typo3OrganizationReader.read()
        } while (read != null && read.name != name)
        return read
    }
}