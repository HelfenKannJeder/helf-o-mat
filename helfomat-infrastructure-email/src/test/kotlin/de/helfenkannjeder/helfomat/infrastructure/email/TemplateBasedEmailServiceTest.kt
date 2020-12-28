package de.helfenkannjeder.helfomat.infrastructure.email

import de.helfenkannjeder.helfomat.core.ProfileRegistry
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.test.context.ActiveProfiles
import java.util.*
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart

@SpringBootTest
@ActiveProfiles(ProfileRegistry.ENABLE_EMAIL)
internal class TemplateBasedEmailServiceTest {

    @MockBean
    private lateinit var emailSender: JavaMailSender

    @Autowired
    private lateinit var templateBasedEmailService: TemplateBasedEmailService

    @ParameterizedTest
    @CsvSource(value = [
        "test-template,This is my subject,This is my test email body",
        "test-with-variable,This is awesome,-",
        "test-with-message,My test message,<span>My test message</span>"
    ], delimiter = ',')
    fun sendEmail_withParsingTemplate_ensureTemplateTextIsCorrect(templatePrefix: String, expectedSubject: String, expectedBody: String) {
        val message = Mockito.mock(MimeMessage::class.java)
        `when`(emailSender.createMimeMessage()).thenReturn(message)

        val attributes = mapOf(Pair("myVariable", "awesome"))
        this.templateBasedEmailService.sendEmail("test@helfenkannjeder.de", templatePrefix, arrayOf("awesome"), attributes, listOf(), Locale.US, null)

        verify(message).setSubject(expectedSubject, "UTF-8")
        val argumentCaptor = ArgumentCaptor.forClass(MimeMultipart::class.java)
        verify(message).setContent(argumentCaptor.capture())
        val multipartMessage = argumentCaptor.value
        val content = multipartMessage.getBodyPart(0).content
        assertThat(content).isInstanceOf(MimeMultipart::class.java)
        val mimeMultipart = content as MimeMultipart
        assertThat(mimeMultipart.getBodyPart(0).content).isEqualTo(expectedBody)
    }

}