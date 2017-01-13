package de.helfenkannjeder.helfomat.dto;

import de.helfenkannjeder.helfomat.domain.Answer;
import de.helfenkannjeder.helfomat.exception.UnsupportedAnswerException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Valentin Zickner
 */
public enum AnswerDto {
    YES, MAYBE, NO;

    public List<AnswerDto> getNeighbours() {
        switch (this) {
            case YES:
                return Collections.singletonList(AnswerDto.MAYBE);
            case MAYBE:
                return Arrays.asList(AnswerDto.YES, AnswerDto.NO);
            case NO:
                return Collections.singletonList(AnswerDto.MAYBE);
        }
        return Collections.emptyList();
    }

    public static AnswerDto fromAnswer(Answer answer) {
        if (answer == null) {
            throw new UnsupportedAnswerException();
        }
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
