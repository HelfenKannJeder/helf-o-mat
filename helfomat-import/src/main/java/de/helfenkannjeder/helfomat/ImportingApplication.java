package de.helfenkannjeder.helfomat;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author Valentin Zickner
 */
@SpringBootApplication
@EnableBatchProcessing
public class ImportingApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ImportingApplication.class, args);
        System.exit(SpringApplication.exit(context));
    }
}
