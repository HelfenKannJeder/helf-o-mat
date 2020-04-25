package de.helfenkannjeder.helfomat;

import de.helfenkannjeder.helfomat.core.organization.OrganizationRepository;
import de.helfenkannjeder.helfomat.core.question.QuestionRepository;
import de.helfenkannjeder.helfomat.core.template.OrganizationTemplateRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * @author Valentin Zickner
 */
@SpringBootApplication
public class TestSpringBootApplication {

    @MockBean
    OrganizationRepository organizationRepository;

    @MockBean
    QuestionRepository questionRepository;

    @MockBean
    OrganizationTemplateRepository organizationTemplateRepository;

}
