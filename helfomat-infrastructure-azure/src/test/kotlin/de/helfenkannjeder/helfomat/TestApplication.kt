package de.helfenkannjeder.helfomat

import de.helfenkannjeder.helfomat.api.CaptchaValidator
import de.helfenkannjeder.helfomat.api.EmailService
import de.helfenkannjeder.helfomat.api.geopoint.DistanceMatrixApplicationService
import de.helfenkannjeder.helfomat.core.approval.ApprovalRepository
import de.helfenkannjeder.helfomat.core.organization.OrganizationRepository
import de.helfenkannjeder.helfomat.core.question.QuestionRepository
import de.helfenkannjeder.helfomat.core.template.OrganizationTemplateRepository
import de.helfenkannjeder.helfomat.core.user.UserRepository
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootApplication
open class TestApplication {

    @MockBean
    lateinit var organizationRepository: OrganizationRepository

    @MockBean
    lateinit var questionRepository: QuestionRepository

    @MockBean
    lateinit var organizationTemplateRepository: OrganizationTemplateRepository

    @MockBean
    private lateinit var distanceMatrixApplicationService: DistanceMatrixApplicationService

    @MockBean
    lateinit var approvalRepository: ApprovalRepository

    @MockBean
    lateinit var captchaValidator: CaptchaValidator

    @MockBean
    lateinit var emailService: EmailService

    @MockBean
    lateinit var userRepository: UserRepository

}