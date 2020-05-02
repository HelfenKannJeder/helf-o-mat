package de.helfenkannjeder.helfomat.infrastructure.filesystem

import de.helfenkannjeder.helfomat.api.picture.RestDownloadService
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.ThrowableAssert.ThrowingCallable
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.web.client.HttpClientErrorException

/**
 * @author Valentin Zickner
 */
@SpringBootTest
@ExtendWith(SpringExtension::class)
internal class RestDownloadServiceTest {

    @Autowired
    private lateinit var downloadService: RestDownloadService


    @Test
    fun downloadPicture_withCorrectUrl_returnsByteArray() {
        // Act
        val bytes: ByteArray? = downloadService.download(getUrl("Radlader_2124_5x2.jpg"))

        // Assert
        assertThat(bytes?.size).isEqualTo(2838526)
        assertThat(bytes?.get(0)).isEqualTo(0xFF.toByte())
        assertThat(bytes?.get(1)).isEqualTo(0xD8.toByte())
    }

    @Test
    fun downloadPicture_withIncorrectUrl_throwsHttpClientErrorException() {
        // Act
        val throwable = ThrowingCallable { downloadService.download(getUrl("test.jpg")) }

        // Assert
        Assertions.assertThatThrownBy(throwable).isInstanceOf(HttpClientErrorException::class.java)
    }

    companion object {

        private val EMBEDDED_HTTP_SERVER = EmbeddedHttpServer()

        @BeforeAll
        @JvmStatic
        fun setUpServer() {
            EMBEDDED_HTTP_SERVER.start()
            EMBEDDED_HTTP_SERVER.setContent("/Radlader_2124_5x2.jpg", null, "de/helfenkannjeder/picture/Radlader_2124_5x2.jpg")
        }

        @AfterAll
        @JvmStatic
        fun tearDownServer() {
            EMBEDDED_HTTP_SERVER.stop()
        }

        private fun getUrl(url: String): String {
            return "http://localhost:${EMBEDDED_HTTP_SERVER.port}/$url"
        }

    }
}