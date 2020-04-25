package de.helfenkannjeder.helfomat.api.picture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

/**
 * @author Valentin Zickner
 */
@Service
public class ResizeImageService {

    private final Logger LOGGER = LoggerFactory.getLogger(ResizeImageService.class);

    public void resize(Path input, Path output, final Integer expectedWidth, final Integer expectedHeight) throws IOException {
        LOGGER.debug("Scale '" + input + "' to size " + expectedWidth + "x" + expectedHeight + " with new name '" + output + "' ");
        BufferedImage image = ImageIO.read(input.toFile());

        int width = image.getWidth();
        int height = image.getHeight();

        if (isNecessaryToScaleHeight(expectedWidth, expectedHeight, image)) {
            width = expectedWidth;
            height = (expectedWidth * image.getHeight()) / image.getWidth();
        } else if (isNecessaryToScaleWidth(expectedWidth, expectedHeight, image)) {
            height = expectedHeight;
            width = (expectedHeight * image.getWidth()) / image.getHeight();
        }

        image = scaleImage(image, width, height);

        if (isNecessaryToCropHeight(expectedHeight, height)) {
            image = cropImage(image, 0, (height - expectedHeight) / 2, expectedWidth, expectedHeight);
        } else if (isNecessaryToCropWidth(expectedWidth, width)) {
            image = cropImage(image, (width - expectedWidth) / 2, 0, expectedWidth, expectedHeight);
        }

        ImageIO.write(image, "PNG", output.toFile());
    }

    private static BufferedImage scaleImage(BufferedImage image, int width, int height) {
        Image tempImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = image.getGraphics();
        graphics.drawImage(tempImage, 0, 0, null);
        graphics.dispose();
        return image;
    }

    private static BufferedImage cropImage(BufferedImage image, int x, int y, int width, int height) {
        image = image.getSubimage(x, y, width, height);
        return image;
    }

    private static boolean isNecessaryToScaleHeight(Integer expectedWidth, Integer expectedHeight, BufferedImage image) {
        return (
            expectedWidth != null && expectedHeight == null
        ) || (
            expectedHeight != null
                && expectedWidth != null
                && image.getWidth() * expectedHeight < expectedWidth * image.getHeight()
        );
    }

    private static boolean isNecessaryToScaleWidth(Integer expectedWidth, Integer expectedHeight, BufferedImage image) {
        return (
            expectedWidth == null && expectedHeight != null
        ) || (
            expectedWidth != null
                && expectedHeight != null
                && image.getWidth() * expectedHeight > expectedWidth * image.getHeight()
        );
    }

    private static boolean isNecessaryToCropHeight(Integer expectedHeight, int height) {
        return expectedHeight != null && height != expectedHeight;
    }

    private static boolean isNecessaryToCropWidth(Integer expectedWidth, int width) {
        return expectedWidth != null && width != expectedWidth;
    }

}
