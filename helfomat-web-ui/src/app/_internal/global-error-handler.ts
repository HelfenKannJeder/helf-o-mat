import {ErrorHandler, Injectable} from "@angular/core";
import {AnalyticsService} from "./analytics.service";

@Injectable()
export class GlobalErrorHandler implements ErrorHandler {

    constructor(private analyticsService: AnalyticsService) {
    }

    handleError(error: any): void {
        const message = error?.message || String(error);
        this.analyticsService.trackEvent('js-error', {
            message: message.substring(0, 200),
        });
        console.error(error);
    }
}
