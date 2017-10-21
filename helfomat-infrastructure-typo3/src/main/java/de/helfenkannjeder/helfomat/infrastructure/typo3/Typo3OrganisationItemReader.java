package de.helfenkannjeder.helfomat.infrastructure.typo3;

import de.helfenkannjeder.helfomat.infrastructure.typo3.domain.TOrganisation;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.item.database.AbstractPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Valentin Zickner
 */
@Component
@JobScope
public class Typo3OrganisationItemReader extends AbstractPagingItemReader<TOrganisation> {

    private Typo3OrganisationRepository typo3OrganisationRepository;

    @Autowired
    public Typo3OrganisationItemReader(Typo3OrganisationRepository typo3OrganisationRepository) {
        this.typo3OrganisationRepository = typo3OrganisationRepository;
        this.setName(this.getClass().getSimpleName());
    }

    @Override
    protected void doReadPage() {
        if (this.results == null) {
            this.results = new CopyOnWriteArrayList<>();
        } else {
            this.results.clear();
        }

        this.results.addAll(this.typo3OrganisationRepository.findAvailable(new PageRequest(getPage(), getPageSize())));
    }

    @Override
    protected void doJumpToPage(int i) {
    }

    public List<TOrganisation> getOrganisations() {
        return this.results;
    }
}
