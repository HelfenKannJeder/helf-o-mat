package de.helfenkannjeder.helfomat.api.contact

import de.helfenkannjeder.helfomat.api.CaptchaValidator
import de.helfenkannjeder.helfomat.api.EmailService
import de.helfenkannjeder.helfomat.api.ratelimit.RateLimiter
import de.helfenkannjeder.helfomat.core.contact.ContactRequestRepository
import de.helfenkannjeder.helfomat.core.organization.OrganizationId
import de.helfenkannjeder.helfomat.core.organization.OrganizationRepository
import de.helfenkannjeder.helfomat.core.ratelimit.RateLimitCounterRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.core.io.Resource
import java.time.Duration
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class ContactApplicationServiceTest {

    @Mock
    private lateinit var organizationRepository: OrganizationRepository

    @Mock
    private lateinit var contactRequestRepository: ContactRequestRepository

    private lateinit var captchaValidator: FakeCaptchaValidator
    private lateinit var emailService: RecordingEmailService
    private lateinit var rateLimiter: FakeRateLimiter
    private lateinit var contactApplicationService: ContactApplicationService

    @BeforeEach
    fun setUp() {
        captchaValidator = FakeCaptchaValidator()
        emailService = RecordingEmailService()
        rateLimiter = FakeRateLimiter()
        contactApplicationService = ContactApplicationService(
            organizationRepository,
            contactRequestRepository,
            captchaValidator,
            emailService,
            rateLimiter,
            ContactFormRateLimitProperties(),
            "https://www.helfenkannjeder.de/",
            "de_DE",
            "",
            "fake@helfenkannjeder.de"
        )
    }

    @Test
    fun sendsTheEmailForARegularSubmission() {
        contactApplicationService.createGeneralContactRequest(generalContactRequest(), CLIENT_IP)

        assertThat(emailService.recipients).containsExactly("fake@helfenkannjeder.de")
    }

    @Test
    fun dropsTheSubmissionWhenTheHoneypotWasFilledIn() {
        contactApplicationService.createGeneralContactRequest(generalContactRequest(website = "http://example.com"), CLIENT_IP)

        assertThat(emailService.recipients).isEmpty()
        // The honeypot is checked first, so a bot does not even cost a captcha verification.
        assertThat(captchaValidator.validations).isEmpty()
    }

    @Test
    fun dropsTheSubmissionWhenTheCaptchaIsNotValid() {
        captchaValidator.valid = false

        contactApplicationService.createGeneralContactRequest(generalContactRequest(), CLIENT_IP)

        assertThat(emailService.recipients).isEmpty()
    }

    @Test
    fun dropsTheSubmissionWhenTheRateLimitIsReached() {
        rateLimiter.allow = false

        contactApplicationService.createGeneralContactRequest(generalContactRequest(), CLIENT_IP)

        assertThat(emailService.recipients).isEmpty()
    }

    @Test
    fun checksTheCaptchaAgainstTheActionTheFormUses() {
        contactApplicationService.createGeneralContactRequest(generalContactRequest(), CLIENT_IP)

        assertThat(captchaValidator.validations).containsExactly("captcha-token" to "submit")
    }

    @Test
    fun countsSubmissionsPerClientAddress() {
        contactApplicationService.createGeneralContactRequest(generalContactRequest(), CLIENT_IP)

        assertThat(rateLimiter.consumed).contains(Triple("contact-form:ip:$CLIENT_IP", 5, Duration.ofHours(1)))
    }

    @Test
    fun countsSubmissionsOfTheSameMailboxAgainstOneBucketNoMatterHowTheAddressIsWritten() {
        contactApplicationService.createGeneralContactRequest(generalContactRequest(email = "m.w.fuji.nam.i@gmail.com"), CLIENT_IP)
        contactApplicationService.createGeneralContactRequest(generalContactRequest(email = "mwfujinami@gmail.com"), CLIENT_IP)

        assertThat(rateLimiter.consumed.filter { it.first.startsWith("contact-form:email:") })
            .containsExactly(
                Triple("contact-form:email:mwfujinami@gmail.com", 3, Duration.ofHours(24)),
                Triple("contact-form:email:mwfujinami@gmail.com", 3, Duration.ofHours(24))
            )
    }

    @Test
    fun storesNothingWhenAnOrganizationContactRequestIsDropped() {
        captchaValidator.valid = false

        val result = contactApplicationService.createContactRequest(organizationContactRequest(), CLIENT_IP)

        // The caller still gets an id back, so a bot cannot tell the submission was thrown away.
        assertThat(result.contactRequestId).isNotNull
        assertThat(emailService.recipients).isEmpty()
        verifyNoInteractions(contactRequestRepository)
        verifyNoInteractions(organizationRepository)
    }

    private fun generalContactRequest(email: String = "someone@example.com", website: String? = null) =
        CreateGeneralContactRequestDto(
            captcha = "captcha-token",
            name = "Max Mustermann",
            email = email,
            subject = "Lob/Kritik",
            message = "Eine Nachricht",
            location = "Karlsruhe",
            address = null,
            website = website
        )

    private fun organizationContactRequest() = CreateContactRequestDto(
        captcha = "captcha-token",
        name = "Max Mustermann",
        email = "someone@example.com",
        subject = "Betreff",
        message = "Eine Nachricht",
        organizationId = OrganizationId(),
        organizationContactPersonIndex = 0
    )

    private class FakeCaptchaValidator : CaptchaValidator {
        var valid = true
        val validations = mutableListOf<Pair<String, String>>()

        override fun validate(captcha: String, expectedAction: String): Boolean {
            validations += captcha to expectedAction
            return valid
        }
    }

    private class FakeRateLimiter : RateLimiter(mock(RateLimitCounterRepository::class.java)) {
        var allow = true
        val consumed = mutableListOf<Triple<String, Int, Duration>>()

        override fun tryConsume(bucket: String, limit: Int, window: Duration): Boolean {
            consumed += Triple(bucket, limit, window)
            return allow
        }
    }

    private class RecordingEmailService : EmailService {
        val recipients = mutableListOf<String>()

        override fun sendEmail(
            to: String,
            templatePrefix: String,
            subjectAttributes: Array<Any>,
            attributes: Map<String, Any?>,
            attachments: List<Triple<String, Resource, String>>,
            locale: Locale,
            replyTo: String?
        ) {
            recipients += to
        }
    }

    companion object {
        private const val CLIENT_IP = "203.0.113.7"
    }

}
