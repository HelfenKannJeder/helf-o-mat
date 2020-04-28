package de.helfenkannjeder.helfomat.web.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import de.helfenkannjeder.helfomat.core.ProfileRegistry
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalTime

@SpringBootTest
@ExtendWith(SpringExtension::class)
@ActiveProfiles(ProfileRegistry.TEST)
class ObjectMapperConfigTest {

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun serializeLocalTime_with4pm_ensureSerializationAsInt() {
        // Arrange
        val localTime = LocalTime.of(16, 0)

        // Act
        val result = objectMapper.writeValueAsString(localTime)

        // Assert
        Assertions.assertThat(result).isEqualTo("\"16:00:00\"")
    }
}