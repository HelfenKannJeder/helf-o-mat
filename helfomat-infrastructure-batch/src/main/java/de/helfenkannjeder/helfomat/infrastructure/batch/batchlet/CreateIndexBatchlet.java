package de.helfenkannjeder.helfomat.infrastructure.batch.batchlet;

import de.helfenkannjeder.helfomat.core.organisation.OrganisationRepository;
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

    private final OrganisationRepository organisationRepository;
    private final Resource organisationMapping;

    public CreateIndexBatchlet(@Qualifier("importOrganisationRepository") OrganisationRepository importOrganisationRepository,
                               @Value("classpath:/mapping/organisation.json") Resource organisationMapping) {
        this.organisationRepository = importOrganisationRepository;
        this.organisationMapping = organisationMapping;
    }

    @Override
    public String process() throws Exception {
        String mapping = StreamUtils.copyToString(organisationMapping.getInputStream(), StandardCharsets.UTF_8);
        this.organisationRepository.createIndex(mapping);
        return ExitStatus.COMPLETED.toString();
    }
}
