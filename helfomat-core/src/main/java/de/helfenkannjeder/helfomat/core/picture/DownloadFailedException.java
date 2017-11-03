package de.helfenkannjeder.helfomat.core.picture;

/**
 * @author Valentin Zickner
 */
public class DownloadFailedException extends Exception {

    public DownloadFailedException() {
    }

    public DownloadFailedException(Exception e) {
        super(e);
    }
}
