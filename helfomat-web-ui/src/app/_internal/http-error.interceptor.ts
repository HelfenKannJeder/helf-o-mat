import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Observable, throwError} from "rxjs";
import {catchError, retry} from "rxjs/operators";
import {ToastrService} from "ngx-toastr";
import {TranslateService} from "@ngx-translate/core";
import {Injectable} from "@angular/core";

@Injectable()
export class HttpErrorInterceptor implements HttpInterceptor {

    constructor(
        private toastr: ToastrService,
        private translateService: TranslateService
    ) {
    }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return next.handle(request)
            .pipe(
                retry(1),
                catchError((error: HttpErrorResponse) => {
                    console.log('Error during communication', error)
                    let errorMessage: string;
                    if (error.error instanceof ErrorEvent) {
                        errorMessage = this.translateService.instant('error.client');
                    } else {
                        errorMessage = this.translateService.instant('error.server');
                    }
                    this.toastr.error(errorMessage);
                    return throwError(error);
                })
            )
    }
}