package de.helfenkannjeder.helfomat.typo3.batch;

import de.helfenkannjeder.helfomat.typo3.domain.TOrganisation;
import de.helfenkannjeder.helfomat.typo3.repository.Typo3OrganisationRepository;
import org.springframework.batch.item.database.AbstractPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Valentin Zickner
 */
@Component
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

        this.typo3OrganisationRepository.findAll(new PageRequest(getPage(), getPageSize())).forEach(this.results::add);
    }

    @Override
    protected void doJumpToPage(int i) {
    }
}
