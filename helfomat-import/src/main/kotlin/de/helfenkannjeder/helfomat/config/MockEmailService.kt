package de.helfenkannjeder.helfomat.config

import de.helfenkannjeder.helfomat.api.EmailService
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import java.util.*

@Service
class MockEmailService : EmailService {

    override fun sendEmail(to: String, templatePrefix: String, subjectAttributes: Array<Any>, attributes: Map<String, Any?>, attachments: List<Triple<String, Resource, String>>, locale: Locale, replyTo: String?) {
        throw UnsupportedOperationException()
    }

}