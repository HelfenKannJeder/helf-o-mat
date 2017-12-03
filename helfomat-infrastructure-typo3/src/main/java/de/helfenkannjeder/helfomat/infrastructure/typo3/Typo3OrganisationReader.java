package de.helfenkannjeder.helfomat.infrastructure.typo3;

import de.helfenkannjeder.helfomat.core.ProfileRegistry;
import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationReader;
import org.springframework.batch.item.database.AbstractPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * @author Valentin Zickner
 */
@Component
@Order(100)
@Transactional(propagation = Propagation.REQUIRED)
@Profile("!" + ProfileRegistry.DISABLE_TYPO3_IMPORT)
public class Typo3OrganisationReader extends AbstractPagingItemReader<Organisation> implements OrganisationReader {

    private Typo3OrganisationRepository typo3OrganisationRepository;
    private Typo3OrganisationProcessor typo3OrganisationProcessor;

    @Autowired
    public Typo3OrganisationReader(Typo3OrganisationRepository typo3OrganisationRepository, Typo3OrganisationProcessor typo3OrganisationProcessor) {
        this.typo3OrganisationRepository = typo3OrganisationRepository;
        this.typo3OrganisationProcessor = typo3OrganisationProcessor;
        this.setName(this.getClass().getSimpleName());
    }

    @Override
    protected void doReadPage() {
        if (this.results == null) {
            this.results = new ArrayList<>();
        } else {
            this.results.clear();
        }

        this.results.addAll(
            this.typo3OrganisationRepository
                .findAvailable(new PageRequest(getPage(), getPageSize()))
            .map(organisation -> this.typo3OrganisationProcessor.process(organisation))
            .collect(Collectors.toList())
        );
    }

    @Override
    protected void doJumpToPage(int i) {
    }

}
