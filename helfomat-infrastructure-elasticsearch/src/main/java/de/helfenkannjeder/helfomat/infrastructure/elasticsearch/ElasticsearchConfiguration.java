package de.helfenkannjeder.helfomat.infrastructure.elasticsearch;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Valentin Zickner
 */
@Component
@ConfigurationProperties("elasticsearch")
public class ElasticsearchConfiguration {

    private String index;

    private TypeConfiguration type;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public TypeConfiguration getType() {
        return type;
    }

    public void setType(TypeConfiguration type) {
        this.type = type;
    }

    public static class TypeConfiguration {
        private String organization;

        public String getOrganization() {
            return organization;
        }

        public void setOrganization(String organization) {
            this.organization = organization;
        }
    }
}
