package de.helfenkannjeder.helfomat.core.approval

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

/**
 * @author Valentin Zickner
 */
interface ApprovalRepository : JpaRepository<Approval, ApprovalId> {

    @Query("SELECT A FROM Approval A WHERE A.approvedDomainEvent IS NULL")
    fun findToApprove(): List<Approval>

}