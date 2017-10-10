package de.helfenkannjeder.helfomat.picture;

/**
 * @author Valentin Zickner
 */
public class DownloadFailedException extends Exception {

    DownloadFailedException() {
    }

    DownloadFailedException(Exception e) {
        super(e);
    }
}
