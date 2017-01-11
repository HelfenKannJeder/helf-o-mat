package de.helfenkannjeder.helfomat.controller;

import de.helfenkannjeder.helfomat.domain.Question;
import de.helfenkannjeder.helfomat.dto.BoundingBoxRequestDto;
import de.helfenkannjeder.helfomat.dto.SearchRequestDto;
import de.helfenkannjeder.helfomat.dto.ClusteredGeoPointDto;
import de.helfenkannjeder.helfomat.dto.OrganisationDto;
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

    @RequestMapping(path = "/organisation/search", method = RequestMethod.POST)
    public List<OrganisationDto> search(@RequestBody SearchRequestDto searchRequestDto) {
        return searchService.findOrganisation(
                        searchRequestDto.getAnswers(),
                        searchRequestDto.getPosition(),
                        searchRequestDto.getDistance()
                );
    }

    @RequestMapping(path = "/organisation/boundingBox", method = RequestMethod.POST)
    public List<ClusteredGeoPointDto> boundingBox(@RequestBody BoundingBoxRequestDto searchRequestDto) {
        return searchService.findClusteredGeoPoints(
                searchRequestDto.getPosition(),
                searchRequestDto.getDistance(),
                searchRequestDto.getBoundingBox(),
                searchRequestDto.getZoom()
        );
    }


}
