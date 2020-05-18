package de.helfenkannjeder.helfomat.api.picture

import de.helfenkannjeder.helfomat.core.picture.PictureId

class PictureAlreadyExistException(pictureId: PictureId) : Exception("Picture with id $pictureId already exist")
