import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Observable, throwError} from "rxjs";
import {catchError, retry} from "rxjs/operators";
import {ToastrService} from "ngx-toastr";
import {TranslateService} from "@ngx-translate/core";
import {Injectable} from "@angular/core";
import {AnalyticsService} from "./analytics.service";

@Injectable()
export class HttpErrorInterceptor implements HttpInterceptor {

    constructor(
        private toastr: ToastrService,
        private translateService: TranslateService,
        private analyticsService: AnalyticsService
    ) {
    }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return next.handle(request)
            .pipe(
                retry(1),
                catchError((error: HttpErrorResponse) => {
                    console.log('Error during communication', error)
                    let errorMessage: string;
                    let errorType: string;
                    if (error.error instanceof ErrorEvent) {
                        errorMessage = this.translateService.instant('error.client');
                        errorType = 'client';
                    } else {
                        errorMessage = this.translateService.instant('error.server');
                        errorType = 'server';
                    }
                    this.toastr.error(errorMessage);
                    this.analyticsService.trackEvent('http-error', {
                        type: errorType,
                        status: error.status,
                        url: HttpErrorInterceptor.extractPath(request.url),
                    });
                    return throwError(error);
                })
            )
    }

    private static extractPath(url: string): string {
        try {
            return new URL(url, window.location.origin).pathname;
        } catch {
            return url;
        }
    }
}