package de.helfenkannjeder.helfomat.infrastructure.batch;

import de.helfenkannjeder.helfomat.core.IndexManager;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Valentin Zickner
 */
@Component
@JobScope
public class BatchIndexManager implements IndexManager {

    private String index;
    private Date date;

    public BatchIndexManager(@Value("#{jobParameters[date]}") Date date,
                             @Value("${elasticsearch.index}") String index) {
        this.index = index;
        this.date = date;
    }

    public String getAlias() {
        return index;
    }

    public String getCurrentIndex() {
        return index + "-" + date.getTime();
    }


}
