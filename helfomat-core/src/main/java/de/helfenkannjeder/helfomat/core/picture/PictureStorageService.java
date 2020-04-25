package de.helfenkannjeder.helfomat.core.picture;

import java.io.InputStream;
import java.nio.file.Path;

/**
 * @author Valentin Zickner
 */
public interface PictureStorageService {

    PictureId savePicture(String url, PictureId pictureId) throws DownloadFailedException;

    PictureId savePicture(byte[] bytes, PictureId pictureId) throws DownloadFailedException;

    PictureId savePicture(PictureId pictureId, InputStream inputStream) throws DownloadFailedException;

    Path getPicture(PictureId pictureId);

    Path getPicture(PictureId pictureId, String size);

    boolean existPicture(PictureId pictureId);
}
