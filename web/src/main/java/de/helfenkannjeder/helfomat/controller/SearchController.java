package de.helfenkannjeder.helfomat.controller;

import de.helfenkannjeder.helfomat.domain.Question;
import de.helfenkannjeder.helfomat.domain.SearchRequest;
import de.helfenkannjeder.helfomat.dto.SearchResultDto;
import de.helfenkannjeder.helfomat.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Valentin Zickner
 */
@RestController
public class SearchController {

    private final SearchService searchService;

    @Autowired
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @RequestMapping(path = "/questions")
    public List<Question> findQuestions() {
        return searchService.findQuestions();
    }

    @RequestMapping(path = "/search", method = RequestMethod.POST)
    public SearchResultDto search(@RequestBody SearchRequest searchRequest) {
        return new SearchResultDto(
                searchService.findOrganisation(
                        searchRequest.getAnswers(),
                        searchRequest.getPosition(),
                        searchRequest.getDistance()
                ),
                searchService.findClusteredGeoPoints(
                        searchRequest.getPosition(),
                        searchRequest.getDistance(),
                        searchRequest.getBoundingBox(),
                        searchRequest.getZoom()
                )
        );
    }

}
