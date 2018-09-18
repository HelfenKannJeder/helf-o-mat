import {Observable} from 'rxjs';
import {Params} from '@angular/router';
import {distinctUntilChanged, map} from "rxjs/operators";

export class ObservableUtil {
    public static extractObjectMember<T>(params: Observable<Params>, property: string): Observable<T> {
        return params
            .pipe(
                map((params: Params) => {
                    if (params.hasOwnProperty(property) && params[property] != 'null') {
                        return params[property];
                    }
                    return null;
                }),
                distinctUntilChanged()
            );
    }
}