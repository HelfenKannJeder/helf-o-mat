package de.helfenkannjeder.helfomat.core.ratelimit

import java.io.Serializable
import java.time.OffsetDateTime
import javax.persistence.Column
import javax.persistence.Embeddable

/**
 * Identifies one counted window: what is being limited and which window it belongs to.
 */
@Embeddable
data class RateLimitCounterId(
    @Column(name = "bucket") val bucket: String = "",

    // No columnDefinition here: the deployed schema comes from the Flyway migration, so leaving the
    // type to the dialect keeps the entity usable on H2 in the tests as well.
    @Column(name = "windowStart") val windowStart: OffsetDateTime = OffsetDateTime.now()
) : Serializable
