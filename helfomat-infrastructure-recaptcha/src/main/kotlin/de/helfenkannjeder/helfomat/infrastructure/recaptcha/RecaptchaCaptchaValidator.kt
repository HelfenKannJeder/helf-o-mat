package de.helfenkannjeder.helfomat.infrastructure.recaptcha

import com.fasterxml.jackson.annotation.JsonProperty
import de.helfenkannjeder.helfomat.api.CaptchaValidator
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import java.util.*

@Service
@EnableConfigurationProperties(RecaptchaConfigurationProperties::class)
class RecaptchaCaptchaValidator(
    private val restTemplate: RestTemplate,
    private val recaptchaConfigurationProperties: RecaptchaConfigurationProperties
) : CaptchaValidator {

    override fun validate(captcha: String): Boolean {
        val requestMap: MultiValueMap<String, String> = LinkedMultiValueMap()
        requestMap.add("secret", recaptchaConfigurationProperties.secret)
        requestMap.add("response", captcha)
        return restTemplate.postForObject(recaptchaConfigurationProperties.endpoint, requestMap, CaptchaResponse::class.java)?.success ?: false
    }

}

private class CaptchaResponse {
    val success: Boolean? = null
    val timestamp: Date? = null
    val hostname: String? = null
    @JsonProperty("error-codes")
    val errorCodes: List<String>? = null
}