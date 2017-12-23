package de.helfenkannjeder.helfomat.infrastructure.batch.batchlet;

import de.helfenkannjeder.helfomat.core.IndexManager;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationRepository;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.beans.factory.annotation.Qualifier;
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
                               @Qualifier("importOrganisationRepository") OrganisationRepository importOrganisationRepository) {
        this.indexManager = indexManager;
        this.organisationRepository = importOrganisationRepository;
    }

    @Override
    public String process() throws Exception {
        this.organisationRepository.updateAlias(this.indexManager.getAlias());

        return COMPLETED.toString();
    }
}
