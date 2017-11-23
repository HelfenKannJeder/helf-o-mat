package de.helfenkannjeder.helfomat.infrastructure;

import de.helfenkannjeder.helfomat.core.IndexManager;
import de.helfenkannjeder.helfomat.core.picture.PictureRepository;
import org.springframework.batch.core.scope.JobScope;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Valentin Zickner
 */
@SpringBootApplication
@EnableJpaRepositories
public class TestApplication {

    @MockBean
    PictureRepository pictureRepository;

    @MockBean
    IndexManager indexManager;

    @Bean
    public JobScope jobScope() {
        JobScope jobScope = new JobScope();
        jobScope.setAutoProxy(false);
        return jobScope;
    }

}
