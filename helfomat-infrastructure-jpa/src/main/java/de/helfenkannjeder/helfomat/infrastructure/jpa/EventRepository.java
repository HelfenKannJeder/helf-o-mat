package de.helfenkannjeder.helfomat.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author Valentin Zickner
 */
public interface EventRepository extends JpaRepository<Event, EventId> {

    @Query("SELECT EP FROM Event EP ORDER BY EP.createdDate")
    List<Event> findAll();
}
