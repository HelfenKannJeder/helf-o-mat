package de.helfenkannjeder.helfomat.service;

import de.helfenkannjeder.helfomat.domain.PictureId;
import org.assertj.core.api.ThrowableAssert;
import org.junit.Test;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Valentin Zickner
 */
public class PictureServiceTest {

    private final PictureService pictureService = new PictureService();

    @Test
    public void getPicture_withValidName_returnsNotNull() throws Exception {
        // Act
        Path picture = pictureService.getPicture(new PictureId(), "test-size");

        // Assert
        assertThat(picture).isNotNull();
    }

    @Test
    public void getPicture_withInvalidName_throwsIllegalArgumentException() throws Exception {
        // Act
        ThrowableAssert.ThrowingCallable runnable = () -> pictureService.getPicture(new PictureId(), "../test");

        // Assert
        assertThatThrownBy(runnable)
            .isNotNull()
            .isInstanceOf(IllegalArgumentException.class);
    }

}