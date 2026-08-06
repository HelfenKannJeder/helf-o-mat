package de.helfenkannjeder.helfomat.api.ratelimit

import de.helfenkannjeder.helfomat.core.ratelimit.RateLimitCounter
import de.helfenkannjeder.helfomat.core.ratelimit.RateLimitCounterRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Duration

@ExtendWith(SpringExtension::class)
@DataJpaTest
@EnableJpaRepositories(basePackageClasses = [RateLimitCounterRepository::class])
@EntityScan(basePackageClasses = [RateLimitCounter::class])
@Import(RateLimiter::class)
internal class RateLimiterTest {

    @Autowired
    private lateinit var rateLimiter: RateLimiter

    @Autowired
    private lateinit var rateLimitCounterRepository: RateLimitCounterRepository

    @Test
    fun allowsHitsUpToTheLimitAndRefusesTheOnesAfterIt() {
        val results = (1..5).map { rateLimiter.tryConsume("contact-form:ip:203.0.113.7", 3, WINDOW) }

        assertThat(results).containsExactly(true, true, true, false, false)
    }

    @Test
    fun countsEachBucketOnItsOwn()  {
        assertThat(rateLimiter.tryConsume("contact-form:ip:198.51.100.1", 1, WINDOW)).isTrue
        assertThat(rateLimiter.tryConsume("contact-form:ip:198.51.100.1", 1, WINDOW)).isFalse

        assertThat(rateLimiter.tryConsume("contact-form:ip:198.51.100.2", 1, WINDOW)).isTrue
    }

    @Test
    fun storesOneRowPerBucketAndWindow() {
        val bucket = "contact-form:email:someone@example.com"

        rateLimiter.tryConsume(bucket, 10, WINDOW)
        rateLimiter.tryConsume(bucket, 10, WINDOW)

        // Counting happens in its own transaction and is therefore committed, so the rows of the
        // other tests are still around and only this bucket can be looked at.
        val counters = rateLimitCounterRepository.findAll().filter { it.rateLimitCounterId.bucket == bucket }
        assertThat(counters).hasSize(1)
        assertThat(counters.first().counter).isEqualTo(2)
    }

    @Test
    fun refusesEverythingWhenTheLimitIsZero() {
        assertThat(rateLimiter.tryConsume("contact-form:ip:203.0.113.9", 0, WINDOW)).isFalse
    }

    companion object {
        private val WINDOW: Duration = Duration.ofHours(1)
    }

}
