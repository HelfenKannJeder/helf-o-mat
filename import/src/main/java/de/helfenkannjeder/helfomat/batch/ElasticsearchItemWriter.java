package de.helfenkannjeder.helfomat.batch;

import java.util.List;
import java.util.stream.Collectors;

import de.helfenkannjeder.helfomat.domain.Organisation;
import de.helfenkannjeder.helfomat.service.IndexManager;
import org.apache.log4j.Logger;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Component;

/**
 * @author Valentin Zickner
 */
@Component
@JobScope
public class ElasticsearchItemWriter implements ItemWriter<Organisation> {

    private static final Logger LOGGER = Logger.getLogger(ElasticsearchItemWriter.class);

    private ElasticsearchTemplate elasticsearchTemplate;
    private String elasticSearchType;
    private IndexManager indexManager;

    @Autowired
    public ElasticsearchItemWriter(ElasticsearchTemplate elasticsearchTemplate,
                                   IndexManager indexManager,
                                   @Value("${elasticsearch.type.organisation}") String type) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.indexManager = indexManager;
        this.elasticSearchType = type;
    }

    @Override
    public void write(List<? extends Organisation> items) throws Exception {
        items.forEach(LOGGER::debug);

        List<IndexQuery> indexQueries = items.stream()
                .map(item -> new IndexQueryBuilder()
                        .withId(String.valueOf(item.getId()))
                        .withObject(item))
                .map(builder -> builder.withType(elasticSearchType))
                .map(builder -> builder.withIndexName(indexManager.getCurrentIndex()))
                .map(IndexQueryBuilder::build)
                .collect(Collectors.toList());

        this.elasticsearchTemplate.bulkIndex(indexQueries);
    }
}
