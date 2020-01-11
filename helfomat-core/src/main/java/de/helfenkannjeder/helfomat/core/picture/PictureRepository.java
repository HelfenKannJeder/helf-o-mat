package de.helfenkannjeder.helfomat.core.picture;

import java.nio.file.Path;

/**
 * @author Valentin Zickner
 */
public interface PictureRepository {

    PictureId savePicture(String url, String folder, PictureId pictureId) throws DownloadFailedException;

    PictureId savePicture(byte[] bytes, String folder, PictureId pictureId) throws DownloadFailedException;

    Path getPicture(PictureId pictureId);

    Path getPicture(PictureId pictureId, String size);

    boolean existPicture(PictureId pictureId);
}
