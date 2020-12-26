package de.helfenkannjeder.helfomat.core.contact

import org.springframework.data.jpa.repository.JpaRepository

/**
 * @author Valentin Zickner
 */
interface ContactRequestRepository : JpaRepository<ContactRequest, ContactRequestId> {
}