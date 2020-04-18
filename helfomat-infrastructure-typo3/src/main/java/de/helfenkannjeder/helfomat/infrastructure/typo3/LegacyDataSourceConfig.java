package de.helfenkannjeder.helfomat.infrastructure.typo3;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Valentin Zickner
 */
@Configuration
@EnableJpaRepositories(
    basePackageClasses = Typo3OrganizationRepository.class,
    entityManagerFactoryRef = "legacyEntityManager",
    transactionManagerRef = "legacyTransactionManager"
)
public class LegacyDataSourceConfig {

    @Bean
    public LocalContainerEntityManagerFactoryBean legacyEntityManager(@Qualifier("legacyDataSource") DataSource legacyDataSource) {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(legacyDataSource);
        entityManagerFactoryBean.setPackagesToScan(this.getClass().getPackage().getName());

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        entityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);
        Map<String, String> jpaProperties = new HashMap<>();
        jpaProperties.put("hibernate.hbm2ddl.auto", "none");
        jpaProperties.put("hibernate.physical_naming_strategy", SpringPhysicalNamingStrategy.class.getName());
        jpaProperties.put("hibernate.implicit_naming_strategy", SpringImplicitNamingStrategy.class.getName());
        entityManagerFactoryBean.setJpaPropertyMap(jpaProperties);

        return entityManagerFactoryBean;
    }

    @Bean
    public PlatformTransactionManager legacyTransactionManager(@Qualifier("legacyEntityManager") LocalContainerEntityManagerFactoryBean legacyEntityManager) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(legacyEntityManager.getObject());
        return transactionManager;
    }

    @Bean
    @ConfigurationProperties("spring.legacy.datasource")
    public DataSourceProperties legacyDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource legacyDataSource(@Qualifier("legacyDataSourceProperties") DataSourceProperties legacyDataSourceProperties) {
        return legacyDataSourceProperties.initializeDataSourceBuilder().build();
    }

}
