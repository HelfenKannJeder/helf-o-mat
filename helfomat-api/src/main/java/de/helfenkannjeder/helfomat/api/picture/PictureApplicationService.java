package de.helfenkannjeder.helfomat.api.picture;

import de.helfenkannjeder.helfomat.api.Roles;
import de.helfenkannjeder.helfomat.core.picture.DownloadFailedException;
import de.helfenkannjeder.helfomat.core.picture.PictureId;
import de.helfenkannjeder.helfomat.core.picture.PictureStorageService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.file.Path;

/**
 * @author Valentin Zickner
 */
@Service
public class PictureApplicationService {

    private final PictureStorageService pictureStorageService;

    public PictureApplicationService(PictureStorageService pictureStorageService) {
        this.pictureStorageService = pictureStorageService;
    }

    public Path getPicture(PictureId pictureId) {
        return pictureStorageService.getPicture(pictureId);
    }

    public Path getPicture(PictureId pictureId, String size) {
        return pictureStorageService.getPicture(pictureId, size);
    }

    @Secured(Roles.ADMIN)
    public void savePicture(PictureId pictureId, InputStream inputStream) throws DownloadFailedException {
        this.pictureStorageService.savePicture(pictureId, inputStream);
    }
}
