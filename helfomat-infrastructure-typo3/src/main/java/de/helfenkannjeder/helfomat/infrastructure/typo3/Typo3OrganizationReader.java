package de.helfenkannjeder.helfomat.infrastructure.typo3;

import de.helfenkannjeder.helfomat.core.ProfileRegistry;
import de.helfenkannjeder.helfomat.core.organization.Organization;
import de.helfenkannjeder.helfomat.core.organization.OrganizationReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Transactional(propagation = Propagation.REQUIRED, transactionManager = "legacyTransactionManager")
@Profile("!" + ProfileRegistry.DISABLE_TYPO3_IMPORT)
public class Typo3OrganizationReader implements OrganizationReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(Typo3OrganizationReader.class);
    private static final int PAGE_SIZE = 10;

    private volatile List<Organization> results = null;
    private volatile int page = 0;
    private volatile int current = 0;


    private final Typo3OrganizationRepository typo3OrganizationRepository;
    private final Typo3OrganizationProcessor typo3OrganizationProcessor;


    @Autowired
    public Typo3OrganizationReader(Typo3OrganizationRepository typo3OrganizationRepository, Typo3OrganizationProcessor typo3OrganizationProcessor) {
        this.typo3OrganizationRepository = typo3OrganizationRepository;
        this.typo3OrganizationProcessor = typo3OrganizationProcessor;
    }

    private void doReadPage() {
        if (this.results == null) {
            this.results = new CopyOnWriteArrayList<>();
        } else {
            this.results.clear();
        }

        this.results.addAll(
            this.typo3OrganizationRepository
                .findAvailable(PageRequest.of(page, PAGE_SIZE))
                .map(this.typo3OrganizationProcessor::process)
                .filter(Objects::nonNull)
                .collect(Collectors.toList())
        );
    }

    @Override
    public String getName() {
        return "typo3";
    }

    @Override
    public Organization read() {
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
