package de.helfenkannjeder.helfomat.web.controller

import de.helfenkannjeder.helfomat.api.question.QuestionApplicationService
import de.helfenkannjeder.helfomat.api.question.QuestionDto
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author Valentin Zickner
 */
@RestController
@RequestMapping(path = ["/api"], produces = [MediaType.APPLICATION_JSON_VALUE])
class QuestionController(private val questionApplicationService: QuestionApplicationService) {

    @GetMapping("/questions")
    fun findQuestions(): List<QuestionDto> = questionApplicationService.findQuestions()

}