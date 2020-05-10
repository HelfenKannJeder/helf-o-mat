package de.helfenkannjeder.helfomat.core.picture

import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.OffsetDateTime
import javax.persistence.Column
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.EntityListeners

@Entity
@EntityListeners(AuditingEntityListener::class)
data class Picture(

    @EmbeddedId
    val pictureId: PictureId,

    var public: Boolean,

    var contentType: String?,

    @CreatedDate
    @Column(columnDefinition = "TIMESTAMPTZ")
    var createdDate: OffsetDateTime? = null,

    @CreatedBy
    var createdBy: String? = null,

    @LastModifiedDate
    @Column(columnDefinition = "TIMESTAMPTZ")
    var updatedDate: OffsetDateTime? = null,

    @LastModifiedBy
    var updatedBy: String? = null

)