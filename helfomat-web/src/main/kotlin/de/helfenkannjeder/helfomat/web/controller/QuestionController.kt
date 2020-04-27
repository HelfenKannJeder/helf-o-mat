package de.helfenkannjeder.helfomat.web.controller;

import de.helfenkannjeder.helfomat.api.question.QuestionApplicationService;
import de.helfenkannjeder.helfomat.api.question.QuestionDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Valentin Zickner
 */
@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class QuestionController {

    private final QuestionApplicationService questionApplicationService;

    public QuestionController(QuestionApplicationService questionApplicationService) {
        this.questionApplicationService = questionApplicationService;
    }

    @GetMapping("/questions")
    public List<QuestionDto> findQuestions() {
        return this.questionApplicationService.findQuestions();
    }

}
