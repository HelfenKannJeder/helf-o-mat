package de.helfenkannjeder.helfomat.web.controller;

import de.helfenkannjeder.helfomat.api.search.BoundingBoxRequestDto;
import de.helfenkannjeder.helfomat.api.search.GeoPointDto;
import de.helfenkannjeder.helfomat.api.search.OrganisationDto;
import de.helfenkannjeder.helfomat.api.search.QuestionDto;
import de.helfenkannjeder.helfomat.api.search.SearchRequestDto;
import de.helfenkannjeder.helfomat.api.search.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Valentin Zickner
 */
@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class SearchController {
    private final SearchService searchService;

    @Autowired
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/questions")
    public List<QuestionDto> findQuestions() {
        return searchService.findQuestions();
    }

    @PostMapping("/organisation/search")
    public List<OrganisationDto> search(@RequestBody SearchRequestDto searchRequestDto) {
        return searchService.findOrganisation(
                        searchRequestDto.getAnswers(),
                        GeoPointDto.toGeoPoint(searchRequestDto.getPosition()),
                        searchRequestDto.getDistance()
                );
    }

    @PostMapping("/organisation/boundingBox")
    public List<GeoPointDto> boundingBox(@RequestBody BoundingBoxRequestDto searchRequestDto) {
        return searchService.findClusteredGeoPoints(
                GeoPointDto.toGeoPoint(searchRequestDto.getPosition()),
                searchRequestDto.getDistance(),
                searchRequestDto.getBoundingBox()
        );
    }
}
