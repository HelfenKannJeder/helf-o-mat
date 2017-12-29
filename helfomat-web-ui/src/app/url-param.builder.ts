import {Answer} from './shared/answer.model';
import {BoundingBox, UserAnswer} from './_internal/resources/organisation.service';
import {GeoPoint} from '../_internal/geopoint';

export class UrlParamBuilder {

    public static buildAnswersFromUserAnswer(userAnswers: UserAnswer[]) {
        // TODO: Should not work like that, because ordering of userAnswers is not ensured
        let answers: Answer[] = [];
        if (userAnswers != null) {
            for (let userAnswer of userAnswers) {
                answers.push(userAnswer.answer);
            }
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
        if (shouldBeInteger == null) {
            return null;
        }
        return parseInt(shouldBeInteger);
    }
}