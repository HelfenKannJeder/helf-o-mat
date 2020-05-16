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

    open fun resize(input: Path, output: Path, expectedWidth: Int?, expectedHeight: Int?, contentType: String?) {
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
        val imageWriters = ImageIO.getImageWritersByMIMEType(contentType ?: "image/jpeg")
        val hasNext = imageWriters.iterator().hasNext()
        if (!hasNext) {
            throw IllegalStateException("Failed to find image writer for '$contentType'")
        }
        val imageWriter = imageWriters.next()
        imageWriter.output = ImageIO.createImageOutputStream(output.toFile())
        imageWriter.write(image)
    }

    private fun scaleImage(inputImage: BufferedImage, width: Int, height: Int): BufferedImage {
        var image = inputImage
        val tempImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH)
        var type = BufferedImage.TYPE_INT_RGB
        if (hasAlphaChannel(inputImage)) {
            type = BufferedImage.TYPE_INT_ARGB
        }
        image = BufferedImage(width, height, type)
        val graphics = image.graphics
        graphics.drawImage(tempImage, 0, 0, null)
        graphics.dispose()
        return image
    }

    private fun hasAlphaChannel(inputImage: BufferedImage) =
        inputImage.type == BufferedImage.TYPE_INT_ARGB ||
            inputImage.type == BufferedImage.TYPE_INT_ARGB_PRE ||
            inputImage.type == BufferedImage.TYPE_4BYTE_ABGR ||
            inputImage.type == BufferedImage.TYPE_4BYTE_ABGR_PRE ||
            inputImage.type == BufferedImage.TYPE_INT_ARGB_PRE

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