package de.helfenkannjeder.helfomat.batch;

import de.helfenkannjeder.helfomat.domain.Organisation;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Valentin Zickner
 */
@Component
@JobScope
public class ElasticsearchItemWriter implements ItemWriter<Organisation> {

    private ElasticsearchTemplate elasticsearchTemplate;
    private Date date;

    @Autowired
    public ElasticsearchItemWriter(ElasticsearchTemplate elasticsearchTemplate,
                                   @Value("#{jobParameters[date]}") Date date) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.date = date;
    }

    @Override
    public void write(List<? extends Organisation> items) throws Exception {
        items.forEach(System.out::println);

        List<IndexQuery> indexQueries = items.stream()
                .map(item -> new IndexQueryBuilder()
                        .withId(String.valueOf(item.getId()))
                        .withObject(item))
                .map(builder -> builder.withType("organisation"))
                .map(builder -> builder.withIndexName("helfomat-" + date.getTime()))
                .map(IndexQueryBuilder::build)
                .collect(Collectors.toList());

        this.elasticsearchTemplate.bulkIndex(indexQueries);
    }
}
