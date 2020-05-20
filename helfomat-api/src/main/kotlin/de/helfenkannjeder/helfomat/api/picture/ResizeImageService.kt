package de.helfenkannjeder.helfomat.api.picture

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import javax.imageio.ImageIO


/**
 * @author Valentin Zickner
 */
@Service
open class ResizeImageService {

    open fun resize(input: InputStream, expectedWidth: Int?, expectedHeight: Int?, contentType: String?): Pair<InputStream, Long> {
        LOGGER.debug("Scale '$input' to size ${expectedWidth ?: "-"}x${expectedHeight ?: "-"}")
        var image = ImageIO.read(input)
        var width = image.width
        var height = image.height
        if (expectedWidth != null && expectedHeight == null || expectedHeight != null && expectedWidth != null && isNecessaryToScaleHeight(expectedWidth, expectedHeight, image)) {
            width = expectedWidth
            height = expectedWidth * image.height / image.width
        } else if (expectedWidth == null && expectedHeight != null || expectedWidth != null && expectedHeight != null && isNecessaryToScaleWidth(expectedWidth, expectedHeight, image)) {
            height = expectedHeight
            width = expectedHeight * image.width / image.height
        }
        val resultContentType = getContentType(contentType)
        image = scaleImage(image, width, height, resultContentType)
        if (expectedHeight != null && height != expectedHeight) {
            image = cropImage(image, 0, (height - expectedHeight) / 2, expectedWidth ?: width, expectedHeight)
        } else if (expectedWidth != null && width != expectedWidth) {
            image = cropImage(image, (width - expectedWidth) / 2, 0, expectedWidth, expectedHeight ?: height)
        }
        val imageWriters = ImageIO.getImageWritersByMIMEType(resultContentType)
        val hasNext = imageWriters.iterator().hasNext()
        if (!hasNext) {
            throw IllegalStateException("Failed to find image writer for '$contentType'")
        }
        val imageWriter = imageWriters.next()
        val byteArrayOutputStream = ByteArrayOutputStream()
        imageWriter.output = ImageIO.createImageOutputStream(byteArrayOutputStream)
        imageWriter.write(image)
        val inputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())
        return Pair(inputStream, byteArrayOutputStream.size().toLong())
    }

    private fun getContentType(contentType: String?) =
        when (contentType) {
            "image/gif" -> "image/png"
            "image/png" -> "image/png"
            else -> "image/jpeg"
        }

    private fun scaleImage(inputImage: BufferedImage, width: Int, height: Int, resultContentType: String): BufferedImage {
        var image = inputImage
        val tempImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH)
        var type = BufferedImage.TYPE_INT_RGB
        if (hasAlphaChannel(resultContentType)) {
            type = BufferedImage.TYPE_INT_ARGB
        }
        image = BufferedImage(width, height, type)
        val graphics = image.graphics
        graphics.drawImage(tempImage, 0, 0, null)
        graphics.dispose()
        return image
    }

    private fun hasAlphaChannel(resultContentType: String) = resultContentType != "image/jpeg"

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