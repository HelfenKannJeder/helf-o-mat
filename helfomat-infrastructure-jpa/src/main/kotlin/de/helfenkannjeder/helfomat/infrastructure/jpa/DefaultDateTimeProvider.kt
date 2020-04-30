package de.helfenkannjeder.helfomat.infrastructure.jpa

import org.springframework.data.auditing.DateTimeProvider
import org.springframework.stereotype.Component
import java.time.OffsetDateTime
import java.time.temporal.TemporalAccessor
import java.util.*

@Component("dateTimeProvider")
internal class DefaultDateTimeProvider : DateTimeProvider {

    override fun getNow(): Optional<TemporalAccessor> {
        return Optional.of(OffsetDateTime.now())
    }

}