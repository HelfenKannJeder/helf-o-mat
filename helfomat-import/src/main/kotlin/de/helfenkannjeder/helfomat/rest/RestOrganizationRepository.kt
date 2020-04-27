package de.helfenkannjeder.helfomat.rest;

import de.helfenkannjeder.helfomat.api.organization.OrganizationAssembler;
import de.helfenkannjeder.helfomat.api.organization.OrganizationDetailDto;
import de.helfenkannjeder.helfomat.api.organization.SearchSimilarOrganizationDto;
import de.helfenkannjeder.helfomat.config.ImporterConfiguration;
import de.helfenkannjeder.helfomat.core.geopoint.BoundingBox;
import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint;
import de.helfenkannjeder.helfomat.core.organization.Address;
import de.helfenkannjeder.helfomat.core.organization.Organization;
import de.helfenkannjeder.helfomat.core.organization.OrganizationRepository;
import de.helfenkannjeder.helfomat.core.organization.OrganizationType;
import de.helfenkannjeder.helfomat.core.organization.QuestionAnswer;
import de.helfenkannjeder.helfomat.core.organization.ScoredOrganization;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

/**
 * @author Valentin Zickner
 */
@Component
public class RestOrganizationRepository implements OrganizationRepository {

    private final RestTemplate restTemplate;
    private final ImporterConfiguration importerConfiguration;

    public RestOrganizationRepository(RestTemplate restTemplate, ImporterConfiguration importerConfiguration) {
        this.restTemplate = restTemplate;
        this.importerConfiguration = importerConfiguration;
    }

    @Override
    public List<Organization> findOrganizationWithSameTypeInDistance(Address defaultAddress, OrganizationType organizationType, Long distanceInMeters) {
        SearchSimilarOrganizationDto searchSimilarOrganizationDto = new SearchSimilarOrganizationDto(
            OrganizationAssembler.toAddressDto(defaultAddress),
            organizationType,
            distanceInMeters
        );
        HttpEntity<SearchSimilarOrganizationDto> requestEntity = new HttpEntity<>(searchSimilarOrganizationDto, new HttpHeaders());
        ResponseEntity<List<OrganizationDetailDto>> result = this.restTemplate.exchange(importerConfiguration.getWebApiUrl() + "/api/organization/search-similar", HttpMethod.POST, requestEntity, new ParameterizedTypeReference<List<OrganizationDetailDto>>() {
        });
        if (result.getBody() == null) {
            return Collections.emptyList();
        }
        return OrganizationAssembler.toOrganizations(result.getBody());
    }

    @Override
    public Organization findByUrlName(String urlName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Organization findOne(String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<ScoredOrganization> findOrganizationsByQuestionAnswersAndDistanceSortByAnswerMatchAndDistance(List<QuestionAnswer> questionAnswers, GeoPoint position, double distance) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Organization> findOrganizationsByDistanceSortByDistance(GeoPoint position, double distance) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<ScoredOrganization> findGlobalOrganizationsByQuestionAnswersSortByAnswerMatch(List<QuestionAnswer> questionAnswers) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Organization> findGlobalOrganizations() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<GeoPoint> findGeoPointsOfOrganizationsInsideBoundingBox(GeoPoint position, double distance, BoundingBox boundingBox) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void save(List<? extends Organization> organizations) {
        throw new UnsupportedOperationException();
    }

}
