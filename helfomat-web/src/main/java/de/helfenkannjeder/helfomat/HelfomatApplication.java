package de.helfenkannjeder.helfomat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;

/**
 * @author Valentin Zickner
 */
@SpringBootApplication(exclude = KafkaAutoConfiguration.class)
public class HelfomatApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelfomatApplication.class, args);
    }

}
