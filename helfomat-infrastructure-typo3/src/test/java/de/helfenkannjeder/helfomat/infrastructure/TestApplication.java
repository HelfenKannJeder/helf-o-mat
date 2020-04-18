package de.helfenkannjeder.helfomat.infrastructure;

import de.helfenkannjeder.helfomat.core.IndexManager;
import de.helfenkannjeder.helfomat.core.picture.PictureRepository;
import org.springframework.batch.core.scope.JobScope;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * @author Valentin Zickner
 */
@SpringBootApplication
public class TestApplication {

    @MockBean
    PictureRepository pictureRepository;

    @MockBean
    IndexManager indexManager;

    @Bean
    @Primary
    @ConfigurationProperties("spring.legacy.datasource")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public JobScope jobScope() {
        JobScope jobScope = new JobScope();
        jobScope.setAutoProxy(false);
        return jobScope;
    }

}
