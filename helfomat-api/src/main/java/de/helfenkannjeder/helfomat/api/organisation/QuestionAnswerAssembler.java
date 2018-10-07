package de.helfenkannjeder.helfomat.api.organisation;

import de.helfenkannjeder.helfomat.core.organisation.QuestionAnswer;
import de.helfenkannjeder.helfomat.core.question.QuestionId;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Valentin Zickner
 */
class QuestionAnswerAssembler {

    static List<QuestionAnswer> toQuestionAnswers(List<QuestionAnswerDto> questionAnswerDtos) {
        if (questionAnswerDtos == null) {
            return null;
        }
        return questionAnswerDtos.stream()
            .map(QuestionAnswerAssembler::toQuestionAnswer)
            .collect(Collectors.toList());
    }

    private static QuestionAnswer toQuestionAnswer(QuestionAnswerDto questionAnswerDto) {
        return new QuestionAnswer(
            new QuestionId(questionAnswerDto.getId()),
            questionAnswerDto.getAnswer());
    }
}
