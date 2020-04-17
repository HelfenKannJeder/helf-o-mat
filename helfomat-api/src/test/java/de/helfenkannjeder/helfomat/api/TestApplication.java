package de.helfenkannjeder.helfomat.api;

import de.helfenkannjeder.helfomat.core.organization.OrganizationRepository;
import de.helfenkannjeder.helfomat.core.question.QuestionRepository;
import de.helfenkannjeder.helfomat.core.template.OrganizationTemplateRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * @author Valentin Zickner
 */
@SpringBootApplication
public class TestApplication {

    @MockBean
    OrganizationRepository organizationRepository;

    @MockBean
    OrganizationTemplateRepository organizationTemplateRepository;

    @MockBean
    QuestionRepository questionRepository;

}
