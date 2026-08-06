package de.helfenkannjeder.helfomat.api.contact

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.time.Duration

@ConstructorBinding
@ConfigurationProperties("helfomat.contact-form.rate-limit")
class ContactFormRateLimitProperties(

    /**
     * Submissions accepted from one client address per [perIpWindow].
     */
    val perIpLimit: Int = 5,
    val perIpWindow: Duration = Duration.ofHours(1),

    /**
     * Submissions accepted for one mailbox per [perEmailWindow].
     */
    val perEmailLimit: Int = 3,
    val perEmailWindow: Duration = Duration.ofHours(24)
)
