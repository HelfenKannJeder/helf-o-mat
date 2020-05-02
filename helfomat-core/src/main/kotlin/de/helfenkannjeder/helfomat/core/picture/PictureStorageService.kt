package de.helfenkannjeder.helfomat.core.picture

import java.io.InputStream
import java.nio.file.Path

/**
 * @author Valentin Zickner
 */
interface PictureStorageService {

    fun savePicture(url: String, pictureId: PictureId): PictureId
    fun savePicture(bytes: ByteArray, pictureId: PictureId): PictureId
    fun savePicture(pictureId: PictureId, inputStream: InputStream): PictureId
    fun getPicture(pictureId: PictureId): Path
    fun getPicture(pictureId: PictureId, size: String): Path
    fun existPicture(pictureId: PictureId): Boolean

}