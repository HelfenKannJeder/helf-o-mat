import {Injectable} from '@angular/core';
import {NavigationEnd, Router} from '@angular/router';
import {filter} from 'rxjs/operators';

export type FlowType = 'questionnaire-with-address' | 'questionnaire-only' | 'direct';

@Injectable({providedIn: 'root'})
export class AnalyticsService {

    private lastTrackedPath: string | null = null;

    constructor(private router: Router) {
        this.router.events
            .pipe(filter(event => event instanceof NavigationEnd))
            .subscribe((event: NavigationEnd) => {
                const cleanedUrl = AnalyticsService.stripMatrixParams(event.urlAfterRedirects || event.url);
                if (cleanedUrl !== this.lastTrackedPath) {
                    this.lastTrackedPath = cleanedUrl;
                    this.trackPageView(cleanedUrl);
                }
            });
    }

    trackEvent(name: string, data?: Record<string, string | number>): void {
        if (typeof umami !== 'undefined') {
            umami.track(name, data);
        }
    }

    private trackPageView(url: string): void {
        if (typeof umami !== 'undefined') {
            umami.track(props => ({...props, url}));
        }
    }

    static stripMatrixParams(url: string): string {
        const [pathAndMatrix, ...fragmentParts] = url.split('#');
        const fragment = fragmentParts.length > 0 ? '#' + fragmentParts.join('#') : '';
        const cleanedPath = pathAndMatrix.replace(/;[^/]*/g, '');
        return cleanedPath + fragment;
    }

    static deriveFlowType(answers: string | null, position: string | null): FlowType {
        const hasAnswers = answers != null && answers !== 'null' && answers !== '[]';
        const hasPosition = position != null && position !== 'null';
        if (!hasAnswers) return 'direct';
        return hasPosition ? 'questionnaire-with-address' : 'questionnaire-only';
    }

}
