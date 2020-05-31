package de.helfenkannjeder.helfomat.infrastructure.email

import de.helfenkannjeder.helfomat.core.ProfileRegistry
import de.helfenkannjeder.helfomat.core.organization.event.ProposedChangeOrganizationEvent
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Profile
import org.springframework.context.event.EventListener
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Component


@Component
@EnableConfigurationProperties(EmailConfiguration::class)
@Profile(ProfileRegistry.ENABLE_EMAIL)
open class ProposedOrganizationChangeEmailListener(
    val emailConfiguration: EmailConfiguration,
    val emailSender: JavaMailSender
) {

    @EventListener
    open fun listen(proposedChangeOrganizationEvent: ProposedChangeOrganizationEvent) {
        val message = SimpleMailMessage()
        message.setTo(emailConfiguration.receiver)
        message.from = emailConfiguration.sender
        message.subject = "HelfenKannJeder Organization Change"
        message.text = "The user ${proposedChangeOrganizationEvent.author} did some changes to an organization.\n" +
            "Please go to ${emailConfiguration.referencedUrl} and check it out.\n" +
            "Thanks!"
        emailSender.send(message)
    }

}