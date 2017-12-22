import {Observable} from 'rxjs/Observable';
import {Params} from '@angular/router';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/filter';
import 'rxjs/add/operator/distinctUntilChanged';

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