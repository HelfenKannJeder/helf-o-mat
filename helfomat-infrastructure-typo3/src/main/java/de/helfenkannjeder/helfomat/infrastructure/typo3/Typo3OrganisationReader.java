package de.helfenkannjeder.helfomat.infrastructure.typo3;

import de.helfenkannjeder.helfomat.core.ProfileRegistry;
import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * @author Valentin Zickner
 */
@Component
@Order(100)
@Transactional(propagation = Propagation.REQUIRED)
@Profile("!" + ProfileRegistry.DISABLE_TYPO3_IMPORT)
@JobScope
public class Typo3OrganisationReader implements OrganisationReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(Typo3OrganisationReader.class);
    private static final int PAGE_SIZE = 10;

    private volatile List<Organisation> results = null;
    private volatile int page = 0;
    private volatile int current = 0;


    private final Typo3OrganisationRepository typo3OrganisationRepository;
    private final Typo3OrganisationProcessor typo3OrganisationProcessor;


    @Autowired
    public Typo3OrganisationReader(Typo3OrganisationRepository typo3OrganisationRepository, Typo3OrganisationProcessor typo3OrganisationProcessor) {
        this.typo3OrganisationRepository = typo3OrganisationRepository;
        this.typo3OrganisationProcessor = typo3OrganisationProcessor;
    }

    private void doReadPage() {
        if (this.results == null) {
            this.results = new CopyOnWriteArrayList<>();
        } else {
            this.results.clear();
        }

        this.results.addAll(
            this.typo3OrganisationRepository
                .findAvailable(PageRequest.of(page, PAGE_SIZE))
                .map(this.typo3OrganisationProcessor::process)
                .filter(Objects::nonNull)
                .collect(Collectors.toList())
        );
    }

    @Override
    public String getName() {
        return "typo3";
    }

    @Override
    public Organisation read() {
        if (results == null || current >= results.size()) {
            LOGGER.debug("Reading page " + page);
            doReadPage();
            page++;
            current = 0;
        }

        int next = current++;
        if (next < results.size()) {
            return results.get(next);
        } else {
            return null;
        }
    }

}
