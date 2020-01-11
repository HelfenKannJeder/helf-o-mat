package de.helfenkannjeder.helfomat.web.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.helfenkannjeder.helfomat.core.ProfileRegistry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles(ProfileRegistry.TEST)
public class ObjectMapperConfigTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void serializeLocalTime_with4pm_ensureSerializationAsInt() throws Exception {
        // Arrange
        LocalTime localTime = LocalTime.of(16, 0);

        // Act
        String result = objectMapper.writeValueAsString(localTime);

        // Assert
        assertThat(result).isEqualTo("\"16:00:00\"");
    }

}
