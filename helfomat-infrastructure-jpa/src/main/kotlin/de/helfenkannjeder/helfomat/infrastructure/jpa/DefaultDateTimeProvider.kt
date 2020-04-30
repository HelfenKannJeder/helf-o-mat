package de.helfenkannjeder.helfomat.infrastructure.jpa;

import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;

@Component("dateTimeProvider")
class DefaultDateTimeProvider implements DateTimeProvider {

    public Optional<TemporalAccessor> getNow() {
        return Optional.of(OffsetDateTime.now());
    }

}