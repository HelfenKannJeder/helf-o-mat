package de.helfenkannjeder.helfomat.api.picture

import java.io.InputStream

data class PictureDto (
    val inputStream: InputStream,
    val contentType: String?
)