package de.helfenkannjeder.helfomat.batch;

import de.helfenkannjeder.helfomat.domain.Organisation;
import de.helfenkannjeder.helfomat.repository.OrganisationRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Valentin Zickner
 */
@Component
public class ElasticsearchItemWriter implements ItemWriter<Organisation> {

    private OrganisationRepository organisationRepository;

    @Autowired
    public ElasticsearchItemWriter(OrganisationRepository organisationRepository) {
        this.organisationRepository = organisationRepository;
    }

    @Override
    public void write(List<? extends Organisation> list) throws Exception {
        list.forEach(System.out::println);
        list.forEach(organisationRepository::save);
    }
}
