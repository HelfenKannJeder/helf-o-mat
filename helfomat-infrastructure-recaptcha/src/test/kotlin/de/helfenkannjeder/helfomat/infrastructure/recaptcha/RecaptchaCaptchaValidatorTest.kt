package de.helfenkannjeder.helfomat.infrastructure.recaptcha

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import org.springframework.test.web.client.response.MockRestResponseCreators.withServerError
import org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess
import org.springframework.web.client.RestTemplate

internal class RecaptchaCaptchaValidatorTest {

    private lateinit var restTemplate: RestTemplate
    private lateinit var mockServer: MockRestServiceServer

    @BeforeEach
    fun setUp() {
        restTemplate = RestTemplate()
        mockServer = MockRestServiceServer.createServer(restTemplate)
    }

    @Test
    fun acceptsAHighScoringTokenForTheExpectedAction() {
        respondWith("""{"success": true, "score": 0.9, "action": "submit", "hostname": "www.helfenkannjeder.de"}""")

        assertThat(validator().validate("token", "submit")).isTrue
    }

    @Test
    fun rejectsALowScoringTokenEvenThoughItIsSuccessful() {
        // This is what the spam submissions look like: a perfectly valid token, scored as a bot.
        respondWith("""{"success": true, "score": 0.1, "action": "submit", "hostname": "www.helfenkannjeder.de"}""")

        assertThat(validator().validate("token", "submit")).isFalse
    }

    @Test
    fun rejectsATokenWithoutAScore() {
        respondWith("""{"success": true, "action": "submit", "hostname": "www.helfenkannjeder.de"}""")

        assertThat(validator().validate("token", "submit")).isFalse
    }

    @Test
    fun rejectsATokenThatWasCreatedForAnotherAction() {
        respondWith("""{"success": true, "score": 0.9, "action": "resubmit", "hostname": "www.helfenkannjeder.de"}""")

        assertThat(validator().validate("token", "submit")).isFalse
    }

    @Test
    fun rejectsATokenSolvedOnAHostnameThatIsNotAllowed() {
        respondWith("""{"success": true, "score": 0.9, "action": "submit", "hostname": "evil.example.com"}""")

        assertThat(validator(allowedHostnames = listOf("www.helfenkannjeder.de")).validate("token", "submit")).isFalse
    }

    @Test
    fun acceptsAnyHostnameWhenNoneAreConfigured() {
        respondWith("""{"success": true, "score": 0.9, "action": "submit", "hostname": "localhost"}""")

        assertThat(validator().validate("token", "submit")).isTrue
    }

    @Test
    fun rejectsAnUnsuccessfulResponse() {
        respondWith("""{"success": false, "error-codes": ["timeout-or-duplicate"]}""")

        assertThat(validator().validate("token", "submit")).isFalse
    }

    @Test
    fun rejectsWhenTheEndpointCannotBeReached() {
        mockServer.expect(requestTo(ENDPOINT)).andRespond(withServerError())

        assertThat(validator().validate("token", "submit")).isFalse
    }

    private fun respondWith(body: String) {
        mockServer.expect(requestTo(ENDPOINT)).andRespond(withSuccess(body, MediaType.APPLICATION_JSON))
    }

    private fun validator(minimumScore: Double = 0.5, allowedHostnames: List<String> = emptyList()) =
        RecaptchaCaptchaValidator(
            restTemplate,
            RecaptchaConfigurationProperties(ENDPOINT, "secret", minimumScore, allowedHostnames)
        )

    companion object {
        private const val ENDPOINT = "https://www.google.com/recaptcha/api/siteverify"
    }

}
