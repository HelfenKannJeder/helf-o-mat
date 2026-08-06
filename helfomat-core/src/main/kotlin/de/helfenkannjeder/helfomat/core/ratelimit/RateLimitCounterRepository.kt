package de.helfenkannjeder.helfomat.core.ratelimit

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.OffsetDateTime

interface RateLimitCounterRepository : JpaRepository<RateLimitCounter, RateLimitCounterId> {

    /**
     * Raises the counter of an existing window and answers how many rows were touched, so the
     * caller can tell whether the window already existed. Doing the increment in the database keeps
     * concurrent requests, also across instances, from overwriting each other's count.
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update RateLimitCounter c set c.counter = c.counter + 1 where c.rateLimitCounterId.bucket = :bucket and c.rateLimitCounterId.windowStart = :windowStart")
    fun increment(@Param("bucket") bucket: String, @Param("windowStart") windowStart: OffsetDateTime): Int

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from RateLimitCounter c where c.rateLimitCounterId.bucket = :bucket and c.rateLimitCounterId.windowStart < :windowStart")
    fun deleteWindowsBefore(@Param("bucket") bucket: String, @Param("windowStart") windowStart: OffsetDateTime): Int

}
