package de.helfenkannjeder.helfomat.core.approval

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

/**
 * @author Valentin Zickner
 */
interface ApprovalRepository : JpaRepository<Approval, ApprovalId> {

    @Query("SELECT A FROM Approval A WHERE A.approvedDomainEvent IS NULL ORDER BY A.createdDate ASC")
    fun findToApprove(): List<Approval>

    @Query("SELECT A FROM Approval A WHERE A.approvedDomainEvent IS NULL AND A.createdBy = :creator")
    fun findToApproveWithCreator(@Param("creator") creator: String): List<Approval>

    @Query("SELECT A FROM Approval A WHERE A.approvedDomainEvent IS NOT NULL ORDER BY A.updatedDate DESC")
    fun findApprovalHistory(pageable: Pageable): List<Approval>

}