package de.helfenkannjeder.helfomat.batch;

import org.elasticsearch.rest.action.admin.indices.alias.delete.AliasesNotFoundException;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.AliasQuery;
import org.springframework.stereotype.Component;

import javax.batch.api.AbstractBatchlet;
import java.util.Date;

import static org.springframework.batch.core.ExitStatus.COMPLETED;

/**
 * @author Valentin Zickner
 */
@Component
@JobScope
public class CreateIndexBatchletStep extends AbstractBatchlet {

    private ElasticsearchTemplate elasticsearchTemplate;
    private Date date;

    @Autowired
    public CreateIndexBatchletStep(ElasticsearchTemplate elasticsearchTemplate,
                                   @Value("#{jobParameters[date]}") Date date) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.date = date;
    }

    @Override
    public String process() throws Exception {
        try {
            AliasQuery removeAliasQuery = new AliasQuery();
            removeAliasQuery.setAliasName("helfomat");
            removeAliasQuery.setIndexName("helfomat-*");
            elasticsearchTemplate.removeAlias(removeAliasQuery);
        } catch (AliasesNotFoundException exception) {
            // Ignore
        }

        AliasQuery aliasQuery = new AliasQuery();
        aliasQuery.setAliasName("helfomat");
        aliasQuery.setIndexName("helfomat-" + date.getTime());
        elasticsearchTemplate.addAlias(aliasQuery);
        return COMPLETED.toString();
    }
}
