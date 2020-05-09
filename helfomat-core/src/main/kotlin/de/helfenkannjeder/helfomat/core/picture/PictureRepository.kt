package de.helfenkannjeder.helfomat.core.picture

import org.springframework.data.jpa.repository.JpaRepository

interface PictureRepository : JpaRepository<Picture, PictureId>