package de.helfenkannjeder.helfomat.importing;

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author Valentin Zickner
 */
@Configuration
public class BatchDataSourceConfig {

    @Bean
    @ConfigurationProperties("spring.batch.datasource")
    public DataSourceProperties batchDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Qualifier("batchDataSource")
    public DataSource batchDataSource(@Qualifier("batchDataSourceProperties") DataSourceProperties batchDataSourceProperties) {
        return batchDataSourceProperties.initializeDataSourceBuilder().build();
    }
    @Configuration
    public class BatchConfigConfigurer extends DefaultBatchConfigurer {

        public BatchConfigConfigurer(@Qualifier("batchDataSource") DataSource dataSource) {
            super(dataSource);
        }
    }
}
