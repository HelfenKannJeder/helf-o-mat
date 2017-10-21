package de.helfenkannjeder.helfomat.infrastructure.typo3;

import de.helfekannjeder.helfomat.core.organisation.Organisation;
import de.helfekannjeder.helfomat.core.organisation.OrganisationReader;
import de.helfenkannjeder.helfomat.infrastructure.typo3.domain.TOrganisation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author Valentin Zickner
 */
@Component
@JobScope
@Order(100)
public class Typo3OrganisationReader implements OrganisationReader {

    private final static Logger LOGGER = LoggerFactory.getLogger(Typo3OrganisationReader.class);

    private final Typo3OrganisationItemReader typo3OrganisationItemReader;
    private final Typo3OrganisationProcessor typo3OrganisationProcessor;

    public Typo3OrganisationReader(Typo3OrganisationItemReader typo3OrganisationItemReader, Typo3OrganisationProcessor typo3OrganisationProcessor) {
        this.typo3OrganisationItemReader = typo3OrganisationItemReader;
        this.typo3OrganisationProcessor = typo3OrganisationProcessor;
    }

    @Override
    public Organisation read() throws Exception {
        Organisation result;
        do {
            TOrganisation tOrganisation = this.typo3OrganisationItemReader.read();
            if (tOrganisation == null) {
                return null;
            }
            result = this.typo3OrganisationProcessor.process(tOrganisation);
        } while (result == null);
        LOGGER.debug("Read organisation from TYPO3 '" + result.getName() + "'");
        return result;
    }

}
