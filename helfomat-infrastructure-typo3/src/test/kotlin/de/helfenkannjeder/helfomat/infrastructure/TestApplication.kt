package de.helfenkannjeder.helfomat.infrastructure

import de.helfenkannjeder.helfomat.core.picture.PictureStorageService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary

/**
 * @author Valentin Zickner
 */
@SpringBootApplication
open class TestApplication {

    @MockBean
    lateinit var pictureStorageService: PictureStorageService

    @Bean
    @Primary
    @ConfigurationProperties("spring.legacy.datasource")
    open fun dataSourceProperties(): DataSourceProperties {
        return DataSourceProperties()
    }

}