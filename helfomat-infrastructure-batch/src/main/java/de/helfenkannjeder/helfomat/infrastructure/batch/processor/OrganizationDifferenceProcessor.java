package de.helfenkannjeder.helfomat.infrastructure.batch.processor;

import de.helfenkannjeder.helfomat.core.organization.Organization;
import de.helfenkannjeder.helfomat.core.organization.OrganizationRepository;
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEvent;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.data.util.Pair;

import java.util.stream.Stream;

/**
 * @author Valentin Zickner
 */
public class OrganizationDifferenceProcessor implements ItemProcessor<Organization, Pair<Organization, Stream<OrganizationEvent>>> {

    private final OrganizationRepository specificOrganizationRepository;
    private final OrganizationRepository generalOrganizationRepository;

    public OrganizationDifferenceProcessor(OrganizationRepository specificOrganizationRepository, OrganizationRepository generalOrganizationRepository) {
        this.specificOrganizationRepository = specificOrganizationRepository;
        this.generalOrganizationRepository = generalOrganizationRepository;
    }

    @Override
    public Pair<Organization, Stream<OrganizationEvent>> process(Organization updatedOrganization) {
        Organization originalOrganization = specificOrganizationRepository.findOrganizationWithSameTypeInDistance(updatedOrganization, 5L);
        if (originalOrganization == null) {
            return generateExistingOrganizationFromOtherDatasource(updatedOrganization);
        }
        return generateUpdateBetweenExistingOrganization(updatedOrganization, originalOrganization);
    }

    private Pair<Organization, Stream<OrganizationEvent>> generateExistingOrganizationFromOtherDatasource(Organization updatedOrganization) {
        Organization alreadyAvailableOrganization = generalOrganizationRepository.findOrganizationWithSameTypeInDistance(updatedOrganization, 5L);
        if (alreadyAvailableOrganization == null) {
            return generateCompleteNewOrganization(updatedOrganization);
        }
        return Pair.of(
            new Organization.Builder(updatedOrganization)
                .setId(alreadyAvailableOrganization.getId())
                .build(),
            Stream.empty()
        );
    }

    private Pair<Organization, Stream<OrganizationEvent>> generateCompleteNewOrganization(Organization updatedOrganization) {
        return generateUpdateBetweenExistingOrganization(updatedOrganization, null);
    }

    private Pair<Organization, Stream<OrganizationEvent>> generateUpdateBetweenExistingOrganization(Organization updatedOrganization, Organization originalOrganization) {
        return Pair.of(
            updatedOrganization,
            updatedOrganization.compareTo(originalOrganization).stream()
        );
    }

}
