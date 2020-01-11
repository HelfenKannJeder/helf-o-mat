package de.helfenkannjeder.helfomat.infrastructure.batch.listener;

import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

/**
 * @author Valentin Zickner
 */
public class UniqueOrganisationUrlNameOrganisationProcessor implements Function<Organisation, Organisation>, StepExecutionListener {

    private final Logger LOG = LoggerFactory.getLogger(UniqueOrganisationUrlNameOrganisationProcessor.class);

    private final Set<String> organisationUrlNames = new HashSet<>();

    @Override
    public Organisation apply(Organisation organisation) {
        if (organisation == null) {
            return null;
        }

        String organisationUrlName = determineOrganisationUrlName(organisation);
        return new Organisation.Builder(organisation)
            .setUrlName(organisationUrlName)
            .build();
    }

    private synchronized String determineOrganisationUrlName(Organisation organisation) {
        String organisationUrlName = toUrlName(organisation.getName());
        int i = 0;
        String originalOrganisationUrlName = organisationUrlName;
        while (organisationUrlNames.contains(organisationUrlName)) {
            organisationUrlName = originalOrganisationUrlName + "-" + (++i);
            LOG.info("Organisation name for '" + originalOrganisationUrlName + "' does already exists, choose new name '" + organisationUrlName + "'");
        }
        organisationUrlNames.add(organisationUrlName);
        return organisationUrlName;
    }

    private static String toUrlName(String organisationName) {
        return organisationName
            .toLowerCase()
            .replaceAll("ä", "ae")
            .replaceAll("ö", "oe")
            .replaceAll("ü", "ue")
            .replaceAll("ß", "ss")
            .replaceAll(" ", "-")
            .replaceAll("[^a-z0-9\\-]", "");
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        this.organisationUrlNames.clear();
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return stepExecution.getExitStatus();
    }
}
