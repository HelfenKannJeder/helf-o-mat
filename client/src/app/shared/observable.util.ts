import {Observable} from 'rxjs/Observable';
import {Params} from '@angular/router';

export class ObservableUtil {
    public static extractObjectMember<T>(params: Observable<Params>, property: string): Observable<T> {
        return params
            .map((params: Params) => {
                if (params.hasOwnProperty(property)) {
                    return params[property];
                }
                return null;
            })
            .filter(element => element != null)
            .distinctUntilChanged();
    }
}