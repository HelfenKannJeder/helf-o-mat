package de.helfenkannjeder.helfomat.picture;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Valentin Zickner
 */
public class ResizeImageServiceTest {

    private Resource inputImage = new ClassPathResource("de/helfenkannjeder/picture/test.png");

    private ResizeImageService resizeImageService;

    @Before
    public void setUp() throws Exception {
        this.resizeImageService = new ResizeImageService();
    }

    @Test
    public void resize_withNoSize_expectFixPoint() throws Exception {
        // Act
        Path result = makeResizeFixPointTest(this.inputImage, null, null);

        // Assert
        assertImageSize(result, 150, 100);
        assertThat(getColor(result, 0, 0)).isEqualTo(Color.BLACK);
        assertThat(getColor(result, 149, 99)).isEqualTo(Color.WHITE);
    }

    @Test
    public void resize_withWidth_expectImageScaled() throws Exception {
        // Act
        Path result = makeResizeFixPointTest(this.inputImage, 105, null);

        // Assert
        assertImageSize(result, 105, 70);
        assertThat(getColor(result, 0, 0)).isEqualTo(Color.BLACK);
        assertThat(getColor(result, 104, 69)).isEqualTo(new Color(254, 254, 254));
    }

    @Test
    public void resize_withHeight_expectImageScaled() throws Exception {
        // Act
        Path result = makeResizeFixPointTest(this.inputImage, null, 70);

        // Assert
        assertImageSize(result, 105, 70);
        assertThat(getColor(result, 0, 0)).isEqualTo(Color.BLACK);
        assertThat(getColor(result, 104, 69)).isEqualTo(new Color(254, 254, 254));
    }

    @Test
    public void resize_widthWidthAndHeight_expectScaleAndHeightToBeCropped() throws Exception {
        // Act
        Path result = makeResizeFixPointTest(this.inputImage, 100, 50);

        // Assert
        assertImageSize(result, 100, 50);
        assertThat(getColor(result, 0, 0)).isEqualTo(new Color(29, 29, 29));
        assertThat(getColor(result, 99, 49)).isEqualTo(new Color(223, 223, 223));
    }

    @Test
    public void resize_widthWidthAndHeight_expectHeightToBeCropped() throws Exception {
        // Act
        Path result = makeResizeFixPointTest(this.inputImage, 150, 50);

        // Assert
        assertImageSize(result, 150, 50);
        assertThat(getColor(result, 0, 0)).isEqualTo(new Color(62, 62, 62));
        assertThat(getColor(result, 149, 49)).isEqualTo(new Color(190, 190, 190));
    }

    @Test
    public void resize_widthWidthAndHeight_expectWidthToBeCropped() throws Exception {
        // Act
        Path result = makeResizeFixPointTest(this.inputImage, 100, 100);

        // Assert
        assertImageSize(result, 100, 100);
        assertThat(getColor(result, 0, 0)).isEqualTo(Color.BLACK);
        assertThat(getColor(result, 99, 99)).isEqualTo(Color.WHITE);
    }

    private static void assertImageSize(Path result, int expectedWidth, int expectedHeight) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(result.toFile());
        assertThat(bufferedImage.getWidth()).isEqualTo(expectedWidth);
        assertThat(bufferedImage.getHeight()).isEqualTo(expectedHeight);
    }

    private Color getColor(Path result, int x, int y) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(result.toFile());
        return new Color(bufferedImage.getRGB(x, y));

    }

    private static File createTempFile() throws IOException {
        File tempFile = File.createTempFile("output", "png");
        tempFile.deleteOnExit();
        return tempFile;
    }

    private Path makeResizeFixPointTest(Resource inputImage, Integer expectedWidth, Integer expectedHeight) throws IOException {
        File tempFile1 = createTempFile();
        File tempFile2 = createTempFile();
        Path inputFile = Paths.get(inputImage.getURI());
        this.resizeImageService.resize(inputFile, tempFile1.toPath(), expectedWidth, expectedHeight);
        this.resizeImageService.resize(tempFile1.toPath(), tempFile2.toPath(), expectedWidth, expectedHeight);
        assertThat(Files.readAllBytes(tempFile2.toPath()))
            .isNotNull()
            .isEqualTo(Files.readAllBytes(tempFile1.toPath()));
        return tempFile2.toPath();
    }
}