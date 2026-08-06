package de.helfenkannjeder.helfomat.api.ratelimit

import de.helfenkannjeder.helfomat.core.ratelimit.RateLimitCounter
import de.helfenkannjeder.helfomat.core.ratelimit.RateLimitCounterId
import de.helfenkannjeder.helfomat.core.ratelimit.RateLimitCounterRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset

@Service
open class RateLimiter(
    private val rateLimitCounterRepository: RateLimitCounterRepository
) {

    /**
     * Counts one hit against [bucket] and answers whether the caller is still within [limit] hits
     * per [window].
     *
     * Windows are fixed and aligned to the epoch, so a caller can send up to twice the limit around
     * a window boundary. That is accurate enough to stop bulk submissions and avoids having to keep
     * a timestamp per hit.
     *
     * Runs in its own transaction so a hit stays counted even when the surrounding request fails
     * afterwards, otherwise the limit could be sidestepped by provoking an error.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    open fun tryConsume(bucket: String, limit: Int, window: Duration): Boolean {
        val windowStart = currentWindowStart(window)

        if (rateLimitCounterRepository.increment(bucket, windowStart) == 0) {
            try {
                rateLimitCounterRepository.saveAndFlush(RateLimitCounter(RateLimitCounterId(bucket, windowStart), 1))
                // The bucket has moved on to a new window, the previous ones are of no use anymore.
                rateLimitCounterRepository.deleteWindowsBefore(bucket, windowStart)
                return limit >= 1
            } catch (e: DataIntegrityViolationException) {
                // Another instance created the same window in between, so fall back to counting up.
                rateLimitCounterRepository.increment(bucket, windowStart)
            }
        }

        val counter = rateLimitCounterRepository.findById(RateLimitCounterId(bucket, windowStart))
            .map(RateLimitCounter::counter)
            .orElse(limit + 1)
        return counter <= limit
    }

    private fun currentWindowStart(window: Duration): OffsetDateTime {
        val epochSecond = OffsetDateTime.now(ZoneOffset.UTC).toEpochSecond()
        val windowSeconds = window.seconds.coerceAtLeast(1)
        return OffsetDateTime.ofInstant(Instant.ofEpochSecond(epochSecond - epochSecond % windowSeconds), ZoneOffset.UTC)
    }

}
