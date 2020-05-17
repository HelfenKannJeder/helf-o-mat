package de.helfenkannjeder.helfomat.api.picture

import de.helfenkannjeder.helfomat.core.picture.PictureId
import java.io.InputStream

/**
 * @author Valentin Zickner
 */
interface PictureStorageService {

    fun savePicture(url: String, pictureId: PictureId)
    fun savePicture(bytes: ByteArray, pictureId: PictureId, contentType: String?)
    fun savePicture(pictureId: PictureId, inputStream: InputStream, fileSize: Long)
    fun savePicture(pictureId: PictureId, inputStream: InputStream, fileSize: Long, tag: String)
    fun getPicture(pictureId: PictureId): InputStream
    fun getPicture(pictureId: PictureId, tag: String): InputStream
    fun existPicture(pictureId: PictureId): Boolean
    fun getContentType(pictureId: PictureId): String?

}