package de.helfenkannjeder.helfomat.repository;

import de.helfenkannjeder.helfomat.domain.GeoPoint;
import de.helfenkannjeder.helfomat.domain.Organisation;
import de.helfenkannjeder.helfomat.service.IndexManager;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.geoDistanceQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.nestedQuery;

@Component
public class OrganisationRepository {
    private Client client;
    private IndexManager indexManager;
    private String type;

    @Autowired
    public OrganisationRepository(Client client,
            IndexManager indexManager,
            @Value("${elasticsearch.type.organisation}") String type) {
        this.client = client;
        this.indexManager = indexManager;
        this.type = type;
    }

    public boolean existsOrganisationWithSameTypeInDistance(Organisation organisation, Long distanceInMeters) {
        GeoPoint locationToCheck = organisation.getAddresses().get(0).getLocation();

        GeoDistanceQueryBuilder geoDistanceQuery = geoDistanceQuery("addresses.location")
                .point(locationToCheck.getLat(), locationToCheck.getLon())
                .distance(distanceInMeters, DistanceUnit.METERS);

        SearchResponse searchResponse = client
                .prepareSearch(indexManager.getCurrentIndex())
                .setTypes(type)
                .setQuery(boolQuery()
                        .must(matchQuery("type", organisation.getType()))
                        .must(nestedQuery("addresses", geoDistanceQuery)))
                .execute()
                .actionGet();

        return searchResponse.getHits().totalHits() > 0;
    }
}
