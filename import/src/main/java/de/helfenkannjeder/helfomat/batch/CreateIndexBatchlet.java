package de.helfenkannjeder.helfomat.batch;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.batch.api.AbstractBatchlet;
import java.nio.charset.Charset;
import java.util.Date;

/**
 * @author Valentin Zickner
 */
@Component
@JobScope
public class CreateIndexBatchlet extends AbstractBatchlet {

    private final Date date;
    private final Resource organisationMapping;
    private final ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    public CreateIndexBatchlet(ElasticsearchTemplate elasticsearchTemplate,
                               @Value("#{jobParameters[date]}") Date date,
                               @Value("classpath:/mapping/organisation.json") Resource organisationMapping) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.date = date;
        this.organisationMapping = organisationMapping;
    }

    @Override
    public String process() throws Exception {
        String mapping = StreamUtils.copyToString(organisationMapping.getInputStream(), Charset.forName("UTF8"));
        String index = "helfomat-" + date.getTime();
        this.elasticsearchTemplate.createIndex(index);
        this.elasticsearchTemplate.putMapping(index, "organisation", mapping);

        return ExitStatus.COMPLETED.toString();
    }
}
