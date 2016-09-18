package de.helfenkannjeder.helfomat.typo3.batch;

import de.helfenkannjeder.helfomat.typo3.domain.TQuestion;
import de.helfenkannjeder.helfomat.typo3.repository.Typo3QuestionRepository;
import org.springframework.batch.item.database.AbstractPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Valentin Zickner
 */
@Component
public class Typo3QuestionItemReader extends AbstractPagingItemReader<TQuestion> {

    private Typo3QuestionRepository typo3QuestionRepository;

    @Autowired
    public Typo3QuestionItemReader(Typo3QuestionRepository typo3QuestionRepository) {
        this.typo3QuestionRepository = typo3QuestionRepository;
        this.setName(this.getClass().getSimpleName());
    }

    @Override
    protected void doReadPage() {
        if (this.results == null) {
            this.results = new CopyOnWriteArrayList<>();
        } else {
            this.results.clear();
        }

        this.typo3QuestionRepository.findAll(new PageRequest(getPage(), getPageSize())).forEach(this.results::add);
    }

    @Override
    protected void doJumpToPage(int i) {
    }
}
