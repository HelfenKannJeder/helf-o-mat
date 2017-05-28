package de.helfenkannjeder.helfomat.controller;

import de.helfenkannjeder.helfomat.dto.BoundingBoxRequestDto;
import de.helfenkannjeder.helfomat.dto.ClusteredGeoPointDto;
import de.helfenkannjeder.helfomat.dto.GeoPointDto;
import de.helfenkannjeder.helfomat.dto.OrganisationDto;
import de.helfenkannjeder.helfomat.dto.QuestionDto;
import de.helfenkannjeder.helfomat.dto.SearchRequestDto;
import de.helfenkannjeder.helfomat.service.SearchService;
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
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
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
    public List<ClusteredGeoPointDto> boundingBox(@RequestBody BoundingBoxRequestDto searchRequestDto) {
        return searchService.findClusteredGeoPoints(
                GeoPointDto.toGeoPoint(searchRequestDto.getPosition()),
                searchRequestDto.getDistance(),
                searchRequestDto.getBoundingBox(),
                searchRequestDto.getZoom()
        );
    }
}
