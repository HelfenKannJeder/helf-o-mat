package de.helfenkannjeder.helfomat.infrastructure.filesystem;

import de.helfenkannjeder.helfomat.api.geopoint.DistanceMatrixApplicationService;
import de.helfenkannjeder.helfomat.api.picture.RestDownloadService;
import org.assertj.core.api.ThrowableAssert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Valentin Zickner
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RestDownloadServiceTest {

    @Autowired
    private RestDownloadService downloadService;

    @MockBean
    private DistanceMatrixApplicationService distanceMatrixApplicationService;

    @BeforeClass
    public static void setUpServer() throws Exception {
        EmbeddedHttpServer.start();
        EmbeddedHttpServer.setContent("/Radlader_2124_5x2.jpg", null, "de/helfenkannjeder/picture/Radlader_2124_5x2.jpg");
    }

    @AfterClass
    public static void tearDownServer() {
        EmbeddedHttpServer.stop();
    }

    @Test
    public void downloadPicture_withCorrectUrl_returnsByteArray() {
        // Act
        byte[] bytes = downloadService.download(getUrl("Radlader_2124_5x2.jpg"));

        // Assert
        assertThat(bytes).isNotNull();
        assertThat(bytes.length).isEqualTo(2838526);
        assertThat(bytes[0]).isEqualTo((byte) 0xFF);
        assertThat(bytes[1]).isEqualTo((byte) 0xD8);
    }

    @Test
    public void downloadPicture_withIncorrectUrl_throwsHttpClientErrorException() {
        // Act
        ThrowableAssert.ThrowingCallable throwable = () ->
            downloadService.download(getUrl("test.jpg"));

        // Assert
        assertThatThrownBy(throwable).isInstanceOf(HttpClientErrorException.class);
    }

    private static String getUrl(String url) {
        return "http://localhost:" + EmbeddedHttpServer.PORT + "/" + url;
    }
}