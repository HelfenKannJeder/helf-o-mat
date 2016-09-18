package de.helfenkannjeder.helfomat.typo3.repository;

import de.helfenkannjeder.helfomat.typo3.domain.TQuestion;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Valentin Zickner
 */
public interface Typo3QuestionRepository extends PagingAndSortingRepository<TQuestion, Long> {
}
