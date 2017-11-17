package de.helfenkannjeder.helfomat.api.picture;

import de.helfenkannjeder.helfomat.api.HelfomatConfiguration;
import de.helfenkannjeder.helfomat.core.picture.DownloadFailedException;
import de.helfenkannjeder.helfomat.core.picture.DownloadService;
import de.helfenkannjeder.helfomat.core.picture.PictureId;
import de.helfenkannjeder.helfomat.core.picture.PictureRepository;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Valentin Zickner
 */
@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class FileSystemPictureRepositoryTest {

    private static final String PICTURE_URL = "https://helfenkannjeder.de/uploads/pics.jpg";
    private static final String FOLDER = "my_folder";
    private static final String CONF_FOLDER = "target/temp/conf_folder";

    @Mock
    private DownloadService downloadService;

    @Mock
    private ResizeImageService resizeImageService;

    @Mock
    private HelfomatConfiguration helfomatConfiguration;

    @Mock
    private HelfomatConfiguration.PictureSize pictureSize;

    private PictureRepository fileSystemPictureRepository;

    @Before
    public void setUp() throws Exception {
        this.fileSystemPictureRepository = new FileSystemPictureRepository(downloadService, resizeImageService, helfomatConfiguration);
    }

    @Test
    public void savePicture_withValidUrl_returnsPictureIdOfDownloadedFile() throws Exception {
        // Arrange
        PictureId pictureId = new PictureId("1fc673b0-f1c8-4d8a-bd6c-c852fd44addf");
        byte[] content = {'a', 'b', 'c', 'd'};
        when(this.downloadService.download(PICTURE_URL)).thenReturn(content);
        when(this.helfomatConfiguration.getPictureFolder()).thenReturn(CONF_FOLDER);

        // Act
        PictureId resultPictureId = this.fileSystemPictureRepository.savePicture(PICTURE_URL, FOLDER, pictureId);

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
            this.fileSystemPictureRepository.savePicture("http://does.not.exist", FOLDER, pictureId);

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
        String configuredFolder = "/*";
        byte[] content = {'a', 'b', 'c', 'd'};
        when(this.downloadService.download(PICTURE_URL)).thenReturn(content);
        when(this.helfomatConfiguration.getPictureFolder()).thenReturn(configuredFolder);

        // Act
        ThrowableAssert.ThrowingCallable throwedException = () ->
            this.fileSystemPictureRepository.savePicture(PICTURE_URL, FOLDER, pictureId);

        // Assert
        assertThatThrownBy(throwedException)
            .isInstanceOf(DownloadFailedException.class);
    }

    @Test
    public void savePicture_withValidUrl_verifyPictureIsScaled() throws Exception {
        // Arrange
        String folderScaled = "test-size";
        int width = 100;
        int height = 200;
        when(pictureSize.getName()).thenReturn(folderScaled);
        when(pictureSize.getWidth()).thenReturn(width);
        when(pictureSize.getHeight()).thenReturn(height);

        PictureId pictureId = new PictureId("1fc673b0-f1c8-4d8a-bd6c-c852fd44addf");
        byte[] content = {'a', 'b', 'c', 'd'};
        when(this.downloadService.download(PICTURE_URL)).thenReturn(content);
        when(this.helfomatConfiguration.getPictureFolder()).thenReturn(CONF_FOLDER);
        when(this.helfomatConfiguration.getPictureSizes()).thenReturn((List) Collections.singletonList(pictureSize));

        // Act
        PictureId resultPictureId = this.fileSystemPictureRepository.savePicture(PICTURE_URL, FOLDER, pictureId);

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


    @Test
    public void getPicture_withValidName_returnsNotNull() throws Exception {
        // Act
        Path picture = this.fileSystemPictureRepository.getPicture(new PictureId(), "test-size");

        // Assert
        assertThat(picture).isNotNull();
    }

    @Test
    public void getPicture_withInvalidName_throwsIllegalArgumentException() throws Exception {
        // Act
        ThrowableAssert.ThrowingCallable runnable = () -> this.fileSystemPictureRepository.getPicture(new PictureId(), "../test");

        // Assert
        assertThatThrownBy(runnable)
            .isNotNull()
            .isInstanceOf(IllegalArgumentException.class);
    }

}