package de.helfenkannjeder.helfomat.infrastructure.google.maps;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Valentin Zickner
 */
@Component
@ConfigurationProperties
public class GoogleMapsConfiguration {

    private String apiKey;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
