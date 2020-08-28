package de.helfenkannjeder.helfomat.core.approval

import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import de.helfenkannjeder.helfomat.core.organization.event.ConfirmedChangeOrganizationEvent
import de.helfenkannjeder.helfomat.core.organization.event.ProposedChangeOrganizationEvent
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
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

/**
 * @author Valentin Zickner
 */
@Entity
@EntityListeners(AuditingEntityListener::class)
@TypeDefs(TypeDef(name = "jsonb", typeClass = JsonBinaryType::class))
data class Approval(
    @EmbeddedId
    val approvalId: ApprovalId,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    val requestedDomainEvent: ProposedChangeOrganizationEvent,

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    var approvedDomainEvent: ConfirmedChangeOrganizationEvent? = null,

    @Column
    var isDeclined: Boolean = false,

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