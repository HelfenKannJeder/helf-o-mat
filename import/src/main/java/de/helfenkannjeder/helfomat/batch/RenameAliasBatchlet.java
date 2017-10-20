package de.helfenkannjeder.helfomat.batch;

import de.helfenkannjeder.helfomat.service.IndexManager;
import org.elasticsearch.rest.action.admin.indices.alias.delete.AliasesNotFoundException;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.AliasQuery;
import org.springframework.stereotype.Component;

import javax.batch.api.AbstractBatchlet;

import static org.springframework.batch.core.ExitStatus.COMPLETED;

/**
 * @author Valentin Zickner
 */
@Component
@JobScope
public class RenameAliasBatchlet extends AbstractBatchlet {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final IndexManager indexManager;

    @Autowired
    public RenameAliasBatchlet(ElasticsearchTemplate elasticsearchTemplate,
                               IndexManager indexManager) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.indexManager = indexManager;
    }

    @Override
    public String process() throws Exception {
        try {
            AliasQuery removeAliasQuery = new AliasQuery();
            removeAliasQuery.setAliasName(indexManager.getAlias());
            removeAliasQuery.setIndexName(indexManager.getAlias() + "-*");
            elasticsearchTemplate.removeAlias(removeAliasQuery);
        } catch (AliasesNotFoundException exception) {
            // Ignore
        }

        AliasQuery aliasQuery = new AliasQuery();
        aliasQuery.setAliasName(indexManager.getAlias());
        aliasQuery.setIndexName(indexManager.getCurrentIndex());
        elasticsearchTemplate.addAlias(aliasQuery);
        return COMPLETED.toString();
    }
}
