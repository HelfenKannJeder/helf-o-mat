package de.helfenkannjeder.helfomat.infrastructure.thwde;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Valentin Zickner
 */
@Component
@ConfigurationProperties(prefix = "crawler.thw")
public class ThwCrawlerConfiguration {
    private String domain;
    private boolean followDomainNames = true;
    private int resultsPerPage;
    private int httpRequestTimeout;
    private String mapPin;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public boolean isFollowDomainNames() {
        return followDomainNames;
    }

    public void setFollowDomainNames(boolean followDomainNames) {
        this.followDomainNames = followDomainNames;
    }

    public int getResultsPerPage() {
        return resultsPerPage;
    }

    public void setResultsPerPage(int resultsPerPage) {
        this.resultsPerPage = resultsPerPage;
    }

    public int getHttpRequestTimeout() {
        return httpRequestTimeout;
    }

    public void setHttpRequestTimeout(int httpRequestTimeout) {
        this.httpRequestTimeout = httpRequestTimeout;
    }

    public String getMapPin() {
        return mapPin;
    }

    public void setMapPin(String mapPin) {
        this.mapPin = mapPin;
    }

}
