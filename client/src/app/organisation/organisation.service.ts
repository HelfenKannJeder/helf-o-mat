import {Injectable} from '@angular/core';
import {Organisation} from './organisation.model';
import {Observable} from 'rxjs';
import {Http, Response} from '@angular/http';
import {GeoPoint} from './geopoint.model';

@Injectable()
export class OrganisationService {

    constructor(private http: Http) {
    }

    getOrganisation(id: string): Observable<Organisation> {
        return this.http.get('api/organisation/' + id)
            .map((response: Response) => response.json());
    }

    getTravelDistances(id: string, location: GeoPoint): Observable<Array<TravelDistance>> {
        return this.http.get(`api/organisation/${id}/travelDistances`, {
            params: {
                lat: location.lat,
                lon: location.lon
            }
        })
            .map((response: Response) => response.json());
    }

}

export class TravelDistance {
    public travelMode: TravelMode;
    public timeInSeconds: number;
    public distanceInMeters: number;
}

export enum TravelMode {
    WALKING = 'WALKING',
    CYCLING = 'CYCLING',
    DRIVING = 'DRIVING',
    TRANSIT = 'TRANSIT'
}