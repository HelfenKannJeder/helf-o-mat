package de.helfenkannjeder.helfomat.api.picture

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.awt.Image
import java.awt.image.BufferedImage
import java.nio.file.Path
import javax.imageio.ImageIO

/**
 * @author Valentin Zickner
 */
@Service
open class ResizeImageService {

    open fun resize(input: Path, output: Path, expectedWidth: Int?, expectedHeight: Int?) {
        LOGGER.debug("Scale '$input' to size ${expectedWidth ?: "-"}x${expectedHeight ?: "-"} with new name '$output' ")
        var image = ImageIO.read(input.toFile())
        var width = image.width
        var height = image.height
        if (expectedWidth != null && expectedHeight == null || expectedHeight != null && expectedWidth != null && isNecessaryToScaleHeight(expectedWidth, expectedHeight, image)) {
            width = expectedWidth
            height = expectedWidth * image.height / image.width
        } else if (expectedWidth == null && expectedHeight != null || expectedWidth != null && expectedHeight != null && isNecessaryToScaleWidth(expectedWidth, expectedHeight, image)) {
            height = expectedHeight
            width = expectedHeight * image.width / image.height
        }
        image = scaleImage(image, width, height)
        if (expectedHeight != null && height != expectedHeight) {
            image = cropImage(image, 0, (height - expectedHeight) / 2, expectedWidth ?: width, expectedHeight)
        } else if (expectedWidth != null && width != expectedWidth) {
            image = cropImage(image, (width - expectedWidth) / 2, 0, expectedWidth, expectedHeight ?: height)
        }
        ImageIO.write(image, "PNG", output.toFile())
    }

    private fun scaleImage(inputImage: BufferedImage, width: Int, height: Int): BufferedImage {
        var image = inputImage
        val tempImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH)
        image = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val graphics = image.graphics
        graphics.drawImage(tempImage, 0, 0, null)
        graphics.dispose()
        return image
    }

    private fun cropImage(inputImage: BufferedImage, x: Int, y: Int, width: Int, height: Int): BufferedImage {
        var image = inputImage
        image = image.getSubimage(x, y, width, height)
        return image
    }

    private fun isNecessaryToScaleHeight(expectedWidth: Int, expectedHeight: Int, image: BufferedImage): Boolean {
        return image.width * expectedHeight < expectedWidth * image.height
    }

    private fun isNecessaryToScaleWidth(expectedWidth: Int, expectedHeight: Int, image: BufferedImage): Boolean {
        return image.width * expectedHeight > expectedWidth * image.height
    }

    companion object {
        private var LOGGER = LoggerFactory.getLogger(ResizeImageService::class.java)

    }

}