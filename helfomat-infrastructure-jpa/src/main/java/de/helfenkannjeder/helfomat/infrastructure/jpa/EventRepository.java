package de.helfenkannjeder.helfomat.infrastructure.jpa;

import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Valentin Zickner
 */
public interface EventRepository extends JpaRepository<Event, EventId> {

    @Query("SELECT EP FROM Event EP ORDER BY EP.createdDate")
    List<Event> findAll();

    @Query("SELECT EP FROM Event EP WHERE EP.organizationId = :organizationId ORDER BY EP.createdDate")
    List<Event> findByOrganizationId(@Param("organizationId") OrganisationId organizationId);
}
