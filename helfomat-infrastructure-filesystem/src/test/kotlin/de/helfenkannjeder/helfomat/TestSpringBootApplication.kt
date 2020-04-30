package de.helfenkannjeder.helfomat

import de.helfenkannjeder.helfomat.api.geopoint.DistanceMatrixApplicationService
import de.helfenkannjeder.helfomat.core.organization.OrganizationRepository
import de.helfenkannjeder.helfomat.core.question.QuestionRepository
import de.helfenkannjeder.helfomat.core.template.OrganizationTemplateRepository
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.mock.mockito.MockBean

/**
 * @author Valentin Zickner
 */
@SpringBootApplication
open class TestSpringBootApplication {
    
    @MockBean
    lateinit var organizationRepository: OrganizationRepository

    @MockBean
    lateinit var questionRepository: QuestionRepository

    @MockBean
    lateinit var organizationTemplateRepository: OrganizationTemplateRepository

    @MockBean
    private lateinit var distanceMatrixApplicationService: DistanceMatrixApplicationService

}