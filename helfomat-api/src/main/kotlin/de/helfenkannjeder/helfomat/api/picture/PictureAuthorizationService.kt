package de.helfenkannjeder.helfomat.api.picture

import de.helfenkannjeder.helfomat.api.Roles
import de.helfenkannjeder.helfomat.api.currentUserHasAnyRole
import de.helfenkannjeder.helfomat.api.currentUsername
import de.helfenkannjeder.helfomat.core.picture.PictureId
import de.helfenkannjeder.helfomat.core.picture.PictureRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class PictureAuthorizationService (
    val pictureRepository: PictureRepository
) {

    fun canAccess(pictureId: PictureId): Boolean {
        val picture = pictureRepository.findByIdOrNull(pictureId) ?: return true
        if (picture.public) {
            return true;
        }

        return currentUsername() == picture.createdBy || currentUserHasAnyRole(Roles.ADMIN, Roles.REVIEWER)
    }

}