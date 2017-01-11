package de.helfenkannjeder.helfomat.dto;

import de.helfenkannjeder.helfomat.domain.Answer;
import de.helfenkannjeder.helfomat.exception.UnsupportedAnswerException;

/**
 * @author Valentin Zickner
 */
public enum AnswerDto {
    YES, MAYBE, NO;

    public static AnswerDto fromAnswer(Answer answer) {
        switch (answer) {
            case MAYBE:
                return AnswerDto.MAYBE;
            case NO:
                return AnswerDto.NO;
            case YES:
                return AnswerDto.YES;
        }
        throw new UnsupportedAnswerException();
    }
}
