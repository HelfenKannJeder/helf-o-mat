package de.helfenkannjeder.helfomat.core.picture

/**
 * @author Valentin Zickner
 */
interface DownloadService {
    fun download(url: String): ByteArray?
}