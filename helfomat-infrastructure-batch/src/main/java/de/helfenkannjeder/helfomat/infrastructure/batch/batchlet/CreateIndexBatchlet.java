package de.helfenkannjeder.helfomat.infrastructure.batch.batchlet;

import de.helfenkannjeder.helfomat.core.organization.OrganizationRepository;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.batch.api.AbstractBatchlet;
import java.nio.charset.StandardCharsets;

/**
 * @author Valentin Zickner
 */
@Component
@JobScope
public class CreateIndexBatchlet extends AbstractBatchlet {

    private final OrganizationRepository organizationRepository;
    private final Resource organizationMapping;

    public CreateIndexBatchlet(@Qualifier("importOrganizationRepository") OrganizationRepository importOrganizationRepository,
                               @Value("classpath:/mapping/organization.json") Resource organizationMapping) {
        this.organizationRepository = importOrganizationRepository;
        this.organizationMapping = organizationMapping;
    }

    @Override
    public String process() throws Exception {
        String mapping = StreamUtils.copyToString(organizationMapping.getInputStream(), StandardCharsets.UTF_8);
        this.organizationRepository.createIndex(mapping);
        return ExitStatus.COMPLETED.toString();
    }
}
