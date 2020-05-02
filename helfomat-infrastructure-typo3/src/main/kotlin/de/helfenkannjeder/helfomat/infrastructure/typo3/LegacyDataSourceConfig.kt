package de.helfenkannjeder.helfomat.infrastructure.typo3

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import java.util.*
import javax.sql.DataSource

/**
 * @author Valentin Zickner
 */
@Configuration
@EnableJpaRepositories(basePackageClasses = [Typo3OrganizationRepository::class], entityManagerFactoryRef = "legacyEntityManager", transactionManagerRef = "legacyTransactionManager")
open class LegacyDataSourceConfig {
    @Bean
    open fun legacyEntityManager(@Qualifier("legacyDataSource") legacyDataSource: DataSource): LocalContainerEntityManagerFactoryBean {
        val entityManagerFactoryBean = LocalContainerEntityManagerFactoryBean()
        entityManagerFactoryBean.dataSource = legacyDataSource
        entityManagerFactoryBean.setPackagesToScan(this.javaClass.getPackage().name)
        val vendorAdapter = HibernateJpaVendorAdapter()
        entityManagerFactoryBean.jpaVendorAdapter = vendorAdapter
        val jpaProperties: MutableMap<String, String?> = HashMap()
        jpaProperties["hibernate.hbm2ddl.auto"] = "none"
        jpaProperties["hibernate.physical_naming_strategy"] = SpringPhysicalNamingStrategy::class.java.name
        jpaProperties["hibernate.implicit_naming_strategy"] = SpringImplicitNamingStrategy::class.java.name
        entityManagerFactoryBean.setJpaPropertyMap(jpaProperties)
        return entityManagerFactoryBean
    }

    @Bean
    open fun legacyTransactionManager(@Qualifier("legacyEntityManager") legacyEntityManager: LocalContainerEntityManagerFactoryBean): PlatformTransactionManager {
        val transactionManager = JpaTransactionManager()
        transactionManager.entityManagerFactory = legacyEntityManager.getObject()
        return transactionManager
    }

    @Bean
    @ConfigurationProperties("spring.legacy.datasource")
    open fun legacyDataSourceProperties(): DataSourceProperties {
        return DataSourceProperties()
    }

    @Bean
    open fun legacyDataSource(@Qualifier("legacyDataSourceProperties") legacyDataSourceProperties: DataSourceProperties): DataSource {
        return legacyDataSourceProperties.initializeDataSourceBuilder().build()
    }
}