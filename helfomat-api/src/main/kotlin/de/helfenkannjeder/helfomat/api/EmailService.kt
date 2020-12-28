package de.helfenkannjeder.helfomat.api

import org.springframework.core.io.Resource
import java.util.*

/**
 * @author Valentin Zickner
 */
interface EmailService {
    fun sendEmail(to: String, templatePrefix: String, subjectAttributes: Array<Any>, attributes: Map<String, Any?>, attachments: List<Triple<String, Resource, String>>, locale: Locale, replyTo: String?)
}