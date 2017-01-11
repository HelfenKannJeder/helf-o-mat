package de.helfenkannjeder.helfomat.service;

import java.util.Date;

import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@JobScope
public class IndexManager {

    private String index;
    private Date date;

    public IndexManager(@Value("#{jobParameters[date]}") Date date,
            @Value("${elasticsearch.index}") String index) {
        this.index = index;
        this.date = date;
    }

    public String getCurrentIndex() {
        return index + "-" + date.getTime();
    }
}
