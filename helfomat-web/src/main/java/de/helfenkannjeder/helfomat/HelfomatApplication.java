package de.helfenkannjeder.helfomat;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Valentin Zickner
 */
@SpringBootApplication
@EnableBatchProcessing
@EnableJpaRepositories
public class HelfomatApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelfomatApplication.class, args);
    }

}
