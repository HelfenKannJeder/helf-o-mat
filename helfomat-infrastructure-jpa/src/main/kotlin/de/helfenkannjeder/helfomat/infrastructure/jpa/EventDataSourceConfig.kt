package de.helfenkannjeder.helfomat.infrastructure.jpa

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.transaction.PlatformTransactionManager
import java.util.*
import javax.sql.DataSource

/**
 * @author Valentin Zickner
 */
@Configuration
@EnableJpaRepositories(basePackageClasses = [Event::class], entityManagerFactoryRef = "eventEntityManager", transactionManagerRef = "eventTransactionManager")
@EnableJpaAuditing(dateTimeProviderRef = "dateTimeProvider")
open class EventDataSourceConfig {

    @Bean
    @Primary
    open fun eventEntityManager(@Qualifier("eventDataSource") eventDataSource: DataSource): LocalContainerEntityManagerFactoryBean {
        val entityManagerFactoryBean = LocalContainerEntityManagerFactoryBean()
        entityManagerFactoryBean.dataSource = eventDataSource
        entityManagerFactoryBean.setPackagesToScan(this.javaClass.getPackage().name)
        val vendorAdapter = HibernateJpaVendorAdapter()
        entityManagerFactoryBean.jpaVendorAdapter = vendorAdapter
        val jpaProperties: MutableMap<String, String> = hashMapOf()
        jpaProperties["hibernate.hbm2ddl.auto"] = "update"
        jpaProperties["hibernate.physical_naming_strategy"] = SpringPhysicalNamingStrategy::class.java.name
        jpaProperties["hibernate.implicit_naming_strategy"] = SpringImplicitNamingStrategy::class.java.name
        entityManagerFactoryBean.setJpaPropertyMap(jpaProperties)
        return entityManagerFactoryBean
    }

    @Bean
    open fun eventTransactionManager(@Qualifier("eventEntityManager") eventEntityManager: LocalContainerEntityManagerFactoryBean): PlatformTransactionManager {
        val transactionManager = JpaTransactionManager()
        transactionManager.entityManagerFactory = eventEntityManager.getObject()
        return transactionManager
    }

    @Bean
    @Primary
    @ConfigurationProperties("spring.event.datasource")
    open fun eventDataSourceProperties(): DataSourceProperties {
        return DataSourceProperties()
    }

    @Bean
    @Primary
    open fun eventDataSource(@Qualifier("eventDataSourceProperties") eventDataSourceProperties: DataSourceProperties): DataSource {
        return eventDataSourceProperties.initializeDataSourceBuilder().build()
    }

    @Bean
    open fun auditor(): AuditorAware<String> {
        return AuditorAware { Optional.ofNullable(SecurityContextHolder.getContext()?.authentication?.name) }
    }
}