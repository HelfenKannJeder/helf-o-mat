package de.helfenkannjeder.helfomat.infrastructure.jpa;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Valentin Zickner
 */
@Configuration
@EnableJpaRepositories(
    basePackageClasses = Event.class,
    entityManagerFactoryRef = "eventEntityManager",
    transactionManagerRef = "eventTransactionManager"
)
@EnableJpaAuditing(dateTimeProviderRef = "dateTimeProvider")
public class EventDataSourceConfig {

    @Bean
    public LocalContainerEntityManagerFactoryBean eventEntityManager(@Qualifier("eventDataSource") DataSource eventDataSource) {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(eventDataSource);
        entityManagerFactoryBean.setPackagesToScan(this.getClass().getPackage().getName());

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        entityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);
        Map<String, String> jpaProperties = new HashMap<>();
        jpaProperties.put("hibernate.hbm2ddl.auto", "update");
        jpaProperties.put("hibernate.physical_naming_strategy", SpringPhysicalNamingStrategy.class.getName());
        jpaProperties.put("hibernate.implicit_naming_strategy", SpringImplicitNamingStrategy.class.getName());
        entityManagerFactoryBean.setJpaPropertyMap(jpaProperties);

        return entityManagerFactoryBean;
    }

    @Bean
    public PlatformTransactionManager eventTransactionManager(@Qualifier("eventEntityManager") LocalContainerEntityManagerFactoryBean eventEntityManager) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(eventEntityManager.getObject());
        return transactionManager;
    }

    @Bean
    @ConfigurationProperties("spring.event.datasource")
    public DataSourceProperties eventDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource eventDataSource(@Qualifier("eventDataSourceProperties") DataSourceProperties eventDataSourceProperties) {
        return eventDataSourceProperties.initializeDataSourceBuilder().build();
    }

    @Bean
    AuditorAware<String> auditor() {
        return () -> {
            SecurityContext context = SecurityContextHolder.getContext();
            if (context == null) {
                return Optional.empty();
            }
            Authentication authentication = context.getAuthentication();
            if (authentication == null) {
                return Optional.empty();
            }
            return Optional.ofNullable(authentication.getName());
        };
    }

}
