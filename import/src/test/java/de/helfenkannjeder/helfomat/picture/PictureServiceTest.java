package de.helfenkannjeder.helfomat.picture;

import de.helfenkannjeder.helfomat.configuration.HelfomatConfiguration;
import de.helfenkannjeder.helfomat.domain.PictureId;
import org.assertj.core.api.ThrowableAssert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.HttpClientErrorException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Valentin Zickner
 */
@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class PictureServiceTest {

    private static final String PICTURE_URL = "https://helfenkannjeder.de/uploads/pics.jpg";
    private static final String FOLDER = "my_folder";
    private static final String CONF_FOLDER = "conf_folder";

    @Mock
    private DownloadService downloadService;

    @Mock
    private ResizeImageService resizeImageService;

    @Mock
    private HelfomatConfiguration helfomatConfiguration;

    private PictureService pictureService;

    @Before
    public void setUp() throws Exception {
        this.pictureService = new PictureService(downloadService, resizeImageService, helfomatConfiguration);
    }

    @Test
    public void savePicture_withValidUrl_returnsPictureIdOfDownloadedFile() throws Exception {
        // Arrange
        PictureId pictureId = new PictureId("1fc673b0-f1c8-4d8a-bd6c-c852fd44addf");
        byte[] content = {'a', 'b', 'c', 'd'};
        when(this.downloadService.download(PICTURE_URL)).thenReturn(content);
        when(this.helfomatConfiguration.getPictureFolder()).thenReturn(CONF_FOLDER);

        // Act
        PictureId resultPictureId = this.pictureService.savePicture(PICTURE_URL, FOLDER, pictureId);

        // Assert
        assertThat(resultPictureId)
            .isNotNull()
            .isEqualTo(pictureId);
        verify(this.downloadService).download(PICTURE_URL);
        Path pathOfOutput = Paths.get(CONF_FOLDER, FOLDER, pictureId.getValue());
        assertThat(Files.readAllBytes(pathOfOutput))
            .isNotNull()
            .isEqualTo(content);
        Files.delete(pathOfOutput);
    }

    @Test
    public void savePicture_withInvalidUrl_expectDownloadFailedException() throws Exception {
        // Arrange
        PictureId pictureId = new PictureId("1fc673b0-f1c8-4d8a-bd6c-c852fd44adde");
        when(this.downloadService.download(Matchers.anyString())).thenThrow(HttpClientErrorException.class);
        when(this.helfomatConfiguration.getPictureFolder()).thenReturn(CONF_FOLDER);

        // Act
        ThrowableAssert.ThrowingCallable throwedException = () ->
            this.pictureService.savePicture("http://does.not.exist", FOLDER, pictureId);

        // Assert
        Path pathOfOutput = Paths.get(CONF_FOLDER, FOLDER, pictureId.getValue());
        assertThat(Files.exists(pathOfOutput))
            .isFalse();
        assertThatThrownBy(throwedException)
            .isInstanceOf(DownloadFailedException.class);
    }

    @Test
    public void savePicture_withFileWriteException_expectDownloadFailedException() throws Exception {
        // Arrange
        PictureId pictureId = new PictureId("1fc673b0-f1c8-4d8a-bd6c-c852fd44adda");
        String configuredFolder = "/proc";
        byte[] content = {'a', 'b', 'c', 'd'};
        when(this.downloadService.download(PICTURE_URL)).thenReturn(content);
        when(this.helfomatConfiguration.getPictureFolder()).thenReturn(configuredFolder);


        // Act
        ThrowableAssert.ThrowingCallable throwedException = () ->
            this.pictureService.savePicture(PICTURE_URL, FOLDER, pictureId);

        // Assert
        Path pathOfOutput = Paths.get(configuredFolder, FOLDER, pictureId.getValue());
        assertThat(Files.exists(pathOfOutput))
            .isFalse();
        assertThatThrownBy(throwedException)
            .isInstanceOf(DownloadFailedException.class);
    }

    @Test
    public void savePicture_withValidUrl_verifyPictureIsScaled() throws Exception {
        // Arrange
        HelfomatConfiguration.PictureSize pictureSize = new HelfomatConfiguration.PictureSize();
        String folderScaled = "test-size";
        int width = 100;
        int height = 200;
        pictureSize.setName(folderScaled);
        pictureSize.setWidth(width);
        pictureSize.setHeight(height);

        PictureId pictureId = new PictureId("1fc673b0-f1c8-4d8a-bd6c-c852fd44addf");
        byte[] content = {'a', 'b', 'c', 'd'};
        when(this.downloadService.download(PICTURE_URL)).thenReturn(content);
        when(this.helfomatConfiguration.getPictureFolder()).thenReturn(CONF_FOLDER);
        when(this.helfomatConfiguration.getPictureSizes()).thenReturn(Collections.singletonList(pictureSize));

        // Act
        PictureId resultPictureId = this.pictureService.savePicture(PICTURE_URL, FOLDER, pictureId);

        // Assert
        assertThat(resultPictureId)
            .isNotNull();
        Path output = Paths.get(CONF_FOLDER, FOLDER, pictureId.getValue());
        Path outputScaled = Paths.get(CONF_FOLDER, FOLDER, folderScaled, pictureId.getValue());
        assertThat(output)
            .isNotNull();
        verify(this.resizeImageService).resize(output, outputScaled, width, height);
        Files.delete(output);
    }

}