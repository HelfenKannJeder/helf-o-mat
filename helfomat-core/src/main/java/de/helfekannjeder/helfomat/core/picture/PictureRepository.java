package de.helfekannjeder.helfomat.core.picture;

import de.helfekannjeder.helfomat.core.organisation.PictureId;

/**
 * @author Valentin Zickner
 */
public interface PictureRepository {
    PictureId savePicture(String url, String folder, PictureId pictureId) throws DownloadFailedException;
}
