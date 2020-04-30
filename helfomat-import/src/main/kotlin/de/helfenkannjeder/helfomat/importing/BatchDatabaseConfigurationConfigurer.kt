package de.helfenkannjeder.helfomat.importing;

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author Valentin Zickner
 */
@Configuration
public class BatchDatabaseConfigurationConfigurer extends DefaultBatchConfigurer {

    @Override
    public void setDataSource(DataSource dataSource) {
        // override to do not set datasource even if a datasource exist.
        // initialize will use a Map based JobRepository (instead of database)
        // see https://stackoverflow.com/questions/25077549/spring-batch-without-persisting-metadata-to-database
    }

}
