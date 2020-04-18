package de.helfenkannjeder.helfomat;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Valentin Zickner
 */
@SpringBootApplication
@EnableBatchProcessing
public class ImportingApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ImportingApplication.class);
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(args);
    }
}
