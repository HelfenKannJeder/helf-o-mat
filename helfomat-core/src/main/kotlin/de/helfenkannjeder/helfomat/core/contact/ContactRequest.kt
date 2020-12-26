package de.helfenkannjeder.helfomat.core.contact

import de.helfenkannjeder.helfomat.core.organization.OrganizationId
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.OffsetDateTime
import javax.persistence.*

/**
 * @author Valentin Zickner
 */
@Entity
@EntityListeners(AuditingEntityListener::class)
data class ContactRequest(
    @EmbeddedId
    val contactRequestId: ContactRequestId,

    val email: String,

    val subject: String,

    val message: String,

    val confirmationCode: String,

    val location: String? = null,

    val age: Int? = null,

    val organizationId: OrganizationId,

    @Embedded
    val contactPerson: ContactRequestContactPerson,

    var status: ContactRequestStatus = ContactRequestStatus.CREATED,

    @Column(columnDefinition = "TIMESTAMPTZ")
    var lastConfirmationEmailSent: OffsetDateTime? = null,

    var numberOfConfirmationEmails: Int = 0,

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
) {
    fun markConfirmationAsSent() {
        lastConfirmationEmailSent = OffsetDateTime.now()
        status = ContactRequestStatus.CONFIRMATION_REQUEST_SENT
        numberOfConfirmationEmails++
    }
}
