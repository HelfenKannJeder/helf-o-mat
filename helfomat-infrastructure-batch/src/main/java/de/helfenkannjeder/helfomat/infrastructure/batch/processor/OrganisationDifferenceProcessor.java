package de.helfenkannjeder.helfomat.infrastructure.batch.processor;

import de.helfenkannjeder.helfomat.core.organisation.Organisation;
import de.helfenkannjeder.helfomat.core.organisation.OrganisationRepository;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEvent;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.data.util.Pair;

import java.util.stream.Stream;

/**
 * @author Valentin Zickner
 */
public class OrganisationDifferenceProcessor implements ItemProcessor<Organisation, Pair<Organisation, Stream<OrganisationEvent>>> {

    private final OrganisationRepository specificOrganisationRepository;
    private final OrganisationRepository generalOrganisationRepository;

    public OrganisationDifferenceProcessor(OrganisationRepository specificOrganisationRepository, OrganisationRepository generalOrganisationRepository) {
        this.specificOrganisationRepository = specificOrganisationRepository;
        this.generalOrganisationRepository = generalOrganisationRepository;
    }

    @Override
    public Pair<Organisation, Stream<OrganisationEvent>> process(Organisation updatedOrganisation) {
        Organisation originalOrganisation = specificOrganisationRepository.findOrganisationWithSameTypeInDistance(updatedOrganisation, 5L);
        if (originalOrganisation == null) {
            Organisation alreadyAvailableOrganisation = generalOrganisationRepository.findOrganisationWithSameTypeInDistance(updatedOrganisation, 5L);
            return Pair.of(
                new Organisation.Builder(updatedOrganisation)
                    .setId(alreadyAvailableOrganisation.getId())
                    .build(),
                Stream.empty()
            );
        }
        return Pair.of(
            updatedOrganisation,
            updatedOrganisation.compareTo(originalOrganisation).stream()
        );
    }

}
