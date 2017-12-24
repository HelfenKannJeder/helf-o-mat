package de.helfenkannjeder.helfomat.infrastructure.batch.processor;

import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationRepository;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEvent;
import org.springframework.batch.item.ItemProcessor;

import java.util.stream.Stream;

/**
 * @author Valentin Zickner
 */
public class OrganisationDifferenceProcessor implements ItemProcessor<Organisation, Stream<OrganisationEvent>> {

    private final OrganisationRepository organisationRepository;

    public OrganisationDifferenceProcessor(OrganisationRepository organisationRepository) {
        this.organisationRepository = organisationRepository;
    }

    @Override
    public Stream<OrganisationEvent> process(Organisation updatedOrganisation) {
        Organisation originalOrganisation = organisationRepository.findOrganisationWithSameTypeInDistance(updatedOrganisation, 5L);
        return updatedOrganisation.compareTo(originalOrganisation).stream();
    }

}
