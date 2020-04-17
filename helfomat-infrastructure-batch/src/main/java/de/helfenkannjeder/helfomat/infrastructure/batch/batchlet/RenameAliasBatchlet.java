package de.helfenkannjeder.helfomat.infrastructure.batch.batchlet;

import de.helfenkannjeder.helfomat.core.IndexManager;
import de.helfenkannjeder.helfomat.core.organization.OrganizationRepository;
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
    private final OrganizationRepository organizationRepository;

    public RenameAliasBatchlet(IndexManager indexManager,
                               @Qualifier("importOrganizationRepository") OrganizationRepository importOrganizationRepository) {
        this.indexManager = indexManager;
        this.organizationRepository = importOrganizationRepository;
    }

    @Override
    public String process() {
        this.organizationRepository.updateAlias(this.indexManager.getAlias());

        return COMPLETED.toString();
    }
}
