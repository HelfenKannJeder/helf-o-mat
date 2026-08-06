package de.helfenkannjeder.helfomat.infrastructure.recaptcha

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import de.helfenkannjeder.helfomat.api.CaptchaValidator
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate

@Service
@EnableConfigurationProperties(RecaptchaConfigurationProperties::class)
class RecaptchaCaptchaValidator(
    private val restTemplate: RestTemplate,
    private val recaptchaConfigurationProperties: RecaptchaConfigurationProperties
) : CaptchaValidator {

    override fun validate(captcha: String, expectedAction: String): Boolean {
        val response = verify(captcha) ?: return false

        if (response.success != true) {
            LOGGER.info("Rejected captcha, siteverify was not successful, error-codes={}", response.errorCodes)
            return false
        }

        // With reCAPTCHA v3 `success` only states that the token is well formed and was issued for
        // our site key, which is just as true for a bot as it is for a human. The score is the only
        // field that tells the two apart, so it has to be present and above the threshold.
        val score = response.score
        if (score == null || score < recaptchaConfigurationProperties.minimumScore) {
            LOGGER.warn(
                "Rejected captcha, score {} is below the threshold of {}, action={}, hostname={}",
                score, recaptchaConfigurationProperties.minimumScore, response.action, response.hostname
            )
            return false
        }

        if (response.action != expectedAction) {
            LOGGER.warn("Rejected captcha, action '{}' does not match the expected action '{}'", response.action, expectedAction)
            return false
        }

        val allowedHostnames = recaptchaConfigurationProperties.allowedHostnames
        if (allowedHostnames.isNotEmpty() && response.hostname !in allowedHostnames) {
            LOGGER.warn("Rejected captcha, hostname '{}' is not one of {}", response.hostname, allowedHostnames)
            return false
        }

        return true
    }

    private fun verify(captcha: String): CaptchaResponse? {
        val requestMap: MultiValueMap<String, String> = LinkedMultiValueMap()
        requestMap.add("secret", recaptchaConfigurationProperties.secret)
        requestMap.add("response", captcha)
        return try {
            restTemplate.postForObject(recaptchaConfigurationProperties.endpoint, requestMap, CaptchaResponse::class.java)
        } catch (e: RestClientException) {
            // Without an answer from Google there is no way to tell a human from a bot, so the
            // submission is dropped rather than waved through.
            LOGGER.error("Failed to reach the captcha endpoint, treating the token as invalid", e)
            null
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(RecaptchaCaptchaValidator::class.java)
    }

}

@JsonIgnoreProperties(ignoreUnknown = true)
private class CaptchaResponse {
    val success: Boolean? = null
    val score: Double? = null
    val action: String? = null
    val hostname: String? = null

    @JsonProperty("error-codes")
    val errorCodes: List<String>? = null
}
