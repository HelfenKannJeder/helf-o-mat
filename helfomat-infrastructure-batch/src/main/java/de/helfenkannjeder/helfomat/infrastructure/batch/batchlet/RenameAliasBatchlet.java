package de.helfenkannjeder.helfomat.infrastructure.batch.batchlet;

import de.helfenkannjeder.helfomat.core.IndexManager;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationRepository;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.stereotype.Component;

import javax.batch.api.AbstractBatchlet;

import static org.springframework.batch.core.ExitStatus.COMPLETED;

/**
 * @author Valentin Zickner
 */
@Component
@JobScope
public class RenameAliasBatchlet extends AbstractBatchlet {

    private final IndexManager indexManager;
    private final OrganisationRepository organisationRepository;

    public RenameAliasBatchlet(IndexManager indexManager,
                               OrganisationRepository organisationRepository) {
        this.indexManager = indexManager;
        this.organisationRepository = organisationRepository;
    }

    @Override
    public String process() throws Exception {
        this.organisationRepository.updateAlias(this.indexManager.getCurrentIndex());

        return COMPLETED.toString();
    }
}
