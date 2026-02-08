import {Injectable} from '@angular/core';

export type FlowType = 'questionnaire-with-address' | 'questionnaire-only' | 'direct';

@Injectable({providedIn: 'root'})
export class AnalyticsService {

    trackEvent(name: string, data?: Record<string, string | number>): void {
        if (typeof umami !== 'undefined') {
            umami.track(name, data);
        }
    }

    static deriveFlowType(answers: string | null, position: string | null): FlowType {
        const hasAnswers = answers != null && answers !== 'null' && answers !== '[]';
        const hasPosition = position != null && position !== 'null';
        if (!hasAnswers) return 'direct';
        return hasPosition ? 'questionnaire-with-address' : 'questionnaire-only';
    }

}
