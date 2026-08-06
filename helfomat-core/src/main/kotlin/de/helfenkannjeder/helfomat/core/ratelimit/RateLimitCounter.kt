package de.helfenkannjeder.helfomat.core.ratelimit

import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.Table

/**
 * Number of hits a bucket has taken within one window.
 */
@Entity
@Table(name = "rate_limit")
data class RateLimitCounter(
    @EmbeddedId
    val rateLimitCounterId: RateLimitCounterId = RateLimitCounterId(),

    var counter: Int = 0
)
