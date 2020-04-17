package de.helfenkannjeder.helfomat.infrastructure.batch.listener;

import de.helfenkannjeder.helfomat.core.organization.Organization;
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
public class UniqueOrganizationUrlNameOrganizationProcessor implements Function<Organization, Organization>, StepExecutionListener {

    private final Logger LOG = LoggerFactory.getLogger(UniqueOrganizationUrlNameOrganizationProcessor.class);

    private final Set<String> organizationUrlNames = new HashSet<>();

    @Override
    public Organization apply(Organization organization) {
        if (organization == null) {
            return null;
        }

        String organizationUrlName = determineOrganizationUrlName(organization);
        return new Organization.Builder(organization)
            .setUrlName(organizationUrlName)
            .build();
    }

    private synchronized String determineOrganizationUrlName(Organization organization) {
        String organizationUrlName = toUrlName(organization.getName());
        int i = 0;
        String originalOrganizationUrlName = organizationUrlName;
        while (organizationUrlNames.contains(organizationUrlName)) {
            organizationUrlName = originalOrganizationUrlName + "-" + (++i);
            LOG.info("Organization name for '" + originalOrganizationUrlName + "' does already exists, choose new name '" + organizationUrlName + "'");
        }
        organizationUrlNames.add(organizationUrlName);
        return organizationUrlName;
    }

    private static String toUrlName(String organizationName) {
        return organizationName
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
        this.organizationUrlNames.clear();
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return stepExecution.getExitStatus();
    }
}
