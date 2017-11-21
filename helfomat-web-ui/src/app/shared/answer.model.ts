export enum Answer {
    NO = <any>"NO",
    MAYBE = <any>"MAYBE",
    YES = <any>"YES"
}

export class AnswerUtil {
    public static getNeighbours(answer: Answer): Answer[] {
        switch (answer) {
            case Answer.NO:
                return [Answer.MAYBE];
            case Answer.MAYBE:
                return [Answer.NO, Answer.YES];
            case Answer.YES:
                return [Answer.MAYBE];
            default:
                return [];
        }
    }

    public static areNeighbours(answer1: Answer, answer2: Answer) {
        return AnswerUtil.getNeighbours(answer1).indexOf(answer2) >= 0;
    }
}