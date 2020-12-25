package de.helfenkannjeder.helfomat.api

import de.helfenkannjeder.helfomat.core.organization.OrganizationRepository
import de.helfenkannjeder.helfomat.core.question.QuestionRepository
import de.helfenkannjeder.helfomat.core.template.OrganizationTemplateRepository
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.mock.mockito.MockBean

/**
 * @author Valentin Zickner
 */
@SpringBootApplication
open class TestApplication {
    @MockBean
    lateinit var organizationRepository: OrganizationRepository

    @MockBean
    lateinit var organizationTemplateRepository: OrganizationTemplateRepository

    @MockBean
    lateinit var questionRepository: QuestionRepository

    @MockBean
    lateinit var captchaValidator: CaptchaValidator

}