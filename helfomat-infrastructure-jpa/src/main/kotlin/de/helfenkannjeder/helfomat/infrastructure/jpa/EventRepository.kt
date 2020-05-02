package de.helfenkannjeder.helfomat.infrastructure.jpa

import de.helfenkannjeder.helfomat.core.organization.OrganizationId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

/**
 * @author Valentin Zickner
 */
interface EventRepository : JpaRepository<Event, EventId> {

    @Query("SELECT E FROM Event E ORDER BY E.createdDate")
    override fun findAll(): List<Event>

    @Query("SELECT E FROM Event E WHERE E.organizationId = :organizationId ORDER BY E.createdDate")
    fun findByOrganizationId(@Param("organizationId") organizationId: OrganizationId): List<Event>

}