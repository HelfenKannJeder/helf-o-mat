import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {GeoPoint} from '../organisation/geopoint.model';
import {UrlParamBuilder} from '../url-param.builder';
import {Observable} from 'rxjs/Observable';
import {Subject} from 'rxjs/Subject';

@Component({
    selector: 'location',
    templateUrl: './location.component.html',
    styleUrls: [
        './location.component.scss'
    ]
})
export class LocationComponent implements OnInit {

    private static MIDDLE_OF_GERMANY = new GeoPoint(51.163375, 10.447683);
    private static COMPLETE_GERMANY_ZOOM_LEVEL = 6;

    public position: Observable<GeoPoint>;
    public zoom: Observable<number>;
    public _position$: Subject<GeoPoint>;

    constructor(private router: Router,
                private route: ActivatedRoute) {
        this.position = Observable.of(LocationComponent.MIDDLE_OF_GERMANY);
        this.zoom = Observable.of(LocationComponent.COMPLETE_GERMANY_ZOOM_LEVEL);
        this._position$ = new Subject();
    }

    ngOnInit(): void {
        Observable
            .combineLatest(
                this.route.params,
                this._position$.asObservable()
            )
            .subscribe(([params, position]: [Params, GeoPoint]) => {
                this.router.navigate(['/result', {
                    answers: params.answers,
                    position: UrlParamBuilder.buildGeoPoint(position)
                }])
                    .then(() => {
                        // TODO: Figure out the reason why the result page occurs in the url, but the content
                        // from there is not loaded. It goes to the page, shows still the content from both
                        // sites and do not execute the ngOnInit method. Forcing the reload hide this behaviour
                        // since the side get's completely new initialized - but that's not a real solution.
                        // Maybe it's a bug in angular, than it might get fixed in a further version.
                        // The behaviour has something to do with google maps, since it only occurs during
                        // the triggering out of an angular component. For using the AlternativeMapComponent
                        // this problem does not exists.
                        window.location.reload(true);
                    });
            });
    }

    public continueWithoutLocation(): void {
        this._position$.next(null);
    }
}