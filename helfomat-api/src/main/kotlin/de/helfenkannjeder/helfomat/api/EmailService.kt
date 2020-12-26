package de.helfenkannjeder.helfomat.api

/**
 * @author Valentin Zickner
 */
interface EmailService {
    fun sendEmail(to: String, templatePrefix: String, attributes: Map<String, Any?>)
}