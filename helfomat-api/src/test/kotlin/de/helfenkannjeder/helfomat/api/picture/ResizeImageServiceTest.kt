package de.helfenkannjeder.helfomat.api.picture

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import java.awt.Color
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import javax.imageio.ImageIO

/**
 * @author Valentin Zickner
 */
internal class ResizeImageServiceTest {
    private val inputImage: Resource = ClassPathResource("de/helfenkannjeder/picture/test.png")
    private val transparentImage: Resource = ClassPathResource("de/helfenkannjeder/picture/transparent.png")
    private lateinit var resizeImageService: ResizeImageService

    @BeforeEach
    fun setUp() {
        resizeImageService = ResizeImageService()
    }

    @Test
    fun resize_withNoSize_expectFixPoint() {
        // Act
        val result = makeResizeFixPointTest(inputImage, null, null)

        // Assert
        assertImageSize(result, 150, 100)
        assertThat(getColor(result, 0, 0)).isEqualTo(Color.BLACK)
        assertThat(getColor(result, 149, 99)).isEqualTo(Color.WHITE)
    }

    @Test
    fun resize_withWidth_expectImageScaled() {
        // Act
        val result = makeResizeFixPointTest(inputImage, 105, null)

        // Assert
        assertImageSize(result, 105, 70)
        assertThat(getColor(result, 0, 0)).isEqualTo(Color.BLACK)
        assertThat(getColor(result, 104, 69)).isEqualTo(Color(254, 254, 254))
    }

    @Test
    fun resize_withHeight_expectImageScaled() {
        // Act
        val result = makeResizeFixPointTest(inputImage, null, 70)

        // Assert
        assertImageSize(result, 105, 70)
        assertThat(getColor(result, 0, 0)).isEqualTo(Color.BLACK)
        assertThat(getColor(result, 104, 69)).isEqualTo(Color(254, 254, 254))
    }

    @Test
    fun resize_widthWidthAndHeight_expectScaleAndHeightToBeCropped() {
        // Act
        val result = makeResizeFixPointTest(inputImage, 100, 50)

        // Assert
        assertImageSize(result, 100, 50)
        assertThat(getColor(result, 0, 0)).isEqualTo(Color(29, 29, 29))
        assertThat(getColor(result, 99, 49)).isEqualTo(Color(223, 223, 223))
    }

    @Test
    fun resize_withTransparentImage_expectScaledTransparentImage() {
        // Act
        val result = makeResizeFixPointTest(transparentImage, 10, 10)

        // Assert
        assertImageSize(result, 10, 10)
        assertThat(getAlpha(result, 0, 0)).isEqualTo(0)
        assertThat(getAlpha(result, 9, 9)).isEqualTo(0)
    }

    @Test
    fun resize_widthWidthAndHeight_expectHeightToBeCropped() {
        // Act
        val result = makeResizeFixPointTest(inputImage, 150, 50)

        // Assert
        assertImageSize(result, 150, 50)
        assertThat(getColor(result, 0, 0)).isEqualTo(Color(62, 62, 62))
        assertThat(getColor(result, 149, 49)).isEqualTo(Color(190, 190, 190))
    }

    @Test
    fun resize_widthWidthAndHeight_expectWidthToBeCropped() {
        // Act
        val result = makeResizeFixPointTest(inputImage, 100, 100)

        // Assert
        assertImageSize(result, 100, 100)
        assertThat(getColor(result, 0, 0)).isEqualTo(Color.BLACK)
        assertThat(getColor(result, 99, 99)).isEqualTo(Color.WHITE)
    }

    @Test
    fun resize_withCorruptedImage_expectNotToFail() {
        // Act
        val corruptedInputImage: Resource = ClassPathResource("de/helfenkannjeder/picture/test-asb-scale-problem.jpg")
        val result = makeResizeFixPointTest(corruptedInputImage, 84, null)

        // Assert
        assertImageSize(result, 84, 50)
    }

    private fun getColor(result: Path, x: Int, y: Int): Color {
        val bufferedImage = ImageIO.read(result.toFile())
        return Color(bufferedImage.getRGB(x, y))
    }

    private fun getAlpha(result: Path, x: Int, y: Int): Int {
        val bufferedImage = ImageIO.read(result.toFile())
        val resultAlpha = IntArray(1)
        bufferedImage.alphaRaster.getPixel(x, y, resultAlpha)
        return resultAlpha[0]
    }

    private fun makeResizeFixPointTest(inputImage: Resource, expectedWidth: Int?, expectedHeight: Int?): Path {
        val tempFile1 = createTempFile()
        val tempFile2 = createTempFile()
        val inputFile = Paths.get(inputImage.uri)
        resizeImageService.resize(inputFile, tempFile1.toPath(), expectedWidth, expectedHeight, "image/png")
        resizeImageService.resize(tempFile1.toPath(), tempFile2.toPath(), expectedWidth, expectedHeight, "image/png")
        assertThat(Files.readAllBytes(tempFile2.toPath()))
            .isEqualTo(Files.readAllBytes(tempFile1.toPath()))
        return tempFile2.toPath()
    }

    private fun assertImageSize(result: Path, expectedWidth: Int, expectedHeight: Int) {
        val bufferedImage = ImageIO.read(result.toFile())
        assertThat(bufferedImage.width).isEqualTo(expectedWidth)
        assertThat(bufferedImage.height).isEqualTo(expectedHeight)
    }

    private fun createTempFile(): File {
        val tempFile = File.createTempFile("output", "png")
        tempFile.deleteOnExit()
        return tempFile
    }

}