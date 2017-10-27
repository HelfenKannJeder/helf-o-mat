package de.helfenkannjeder.helfomat.core.question;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Valentin Zickner
 */
public enum Answer {
    YES, MAYBE, NO;

    public List<Answer> getNeighbours() {
        switch (this) {
            case YES:
                return Collections.singletonList(Answer.MAYBE);
            case MAYBE:
                return Arrays.asList(Answer.YES, Answer.NO);
            case NO:
                return Collections.singletonList(Answer.MAYBE);
        }
        return Collections.emptyList();
    }
}
