package de.helfenkannjeder.helfomat.api.picture

import de.helfenkannjeder.helfomat.core.picture.PictureId

class PictureNotFoundException(pictureId: PictureId) : Exception("Picture with id ${pictureId} could not be found")
