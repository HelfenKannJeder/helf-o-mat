import {Answer} from './shared/answer.model';
import {GeoPoint} from './organisation/geopoint.model';
import {UserAnswer} from './organisation/userAnswer.model';
import {BoundingBox} from './organisation/boundingbox.model';

export class UrlParamBuilder {

    public static buildAnswersFromUserAnswer(userAnswers: UserAnswer[]) {
        // TODO: Should not work like that, because ordering of userAnswers is not ensured
        let answers: Answer[] = [];
        for (let userAnswer of userAnswers) {
            answers.push(userAnswer.answer);
        }
        return UrlParamBuilder.buildAnswers(answers);
    }

    public static buildAnswers(answers: Answer[]): string {
        return JSON.stringify(answers);
    }

    public static parseAnswers(answers: string): Answer[] {
        return JSON.parse(answers);
    }

    public static buildGeoPoint(geoPoint: GeoPoint): string {
        return JSON.stringify(geoPoint);
    }

    public static parseGeoPoint(geoPoint: string): GeoPoint {
        return JSON.parse(geoPoint);
    }

    public static buildBoundingBox(boundingBox: BoundingBox) : string {
        return JSON.stringify(boundingBox);
    }

    public static parseInt(shouldBeInteger: string): number {
        return parseInt(shouldBeInteger);
    }
}