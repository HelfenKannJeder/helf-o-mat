package de.helfenkannjeder.helfomat.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Valentin Zickner
 */
@ConfigurationProperties("helfomat.importer")
public class ImporterConfiguration {

    private String webApiUrl;

    public String getWebApiUrl() {
        return webApiUrl;
    }

    public void setWebApiUrl(String webApiUrl) {
        this.webApiUrl = webApiUrl;
    }
}
