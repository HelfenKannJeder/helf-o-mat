import {Injectable} from '@angular/core';
import {Http, Response} from '@angular/http';
import 'rxjs/add/operator/map';
import {AnsweredQuestion} from '../../organisation/answeredQuestion.model';
import {Group} from '../../organisation/group.model';
import {AttendanceTime} from '../../organisation/attendance-time.model';
import {Observable} from 'rxjs/Rx';
import {Answer} from '../../shared/answer.model';
import {GeoPoint} from '../../../_internal/geopoint';

@Injectable()
export class OrganisationService {

    constructor(private http: Http) {
    }

    findGlobal(): Observable<Organisation[]> {
        return this.http
            .get('api/organisation/global')
            .map((response: Response) => response.json());
    }

    findGlobalByQuestionAnswers(answers: UserAnswer[]): Observable<Organisation[]> {
        return this.http
            .post('api/organisation/global/byQuestionAnswers', answers)
            .map((response: Response) => response.json());
    }

    findByPosition(position: GeoPoint, distance: number): Observable<Organisation[]> {
        return this.http
            .get('api/organisation/byPosition', {
                params: {
                    position: GeoPoint.asString(position),
                    distance
                }
            })
            .map((response: Response) => response.json());
    }

    findByQuestionAnswersAndPosition(answers: UserAnswer[], position: GeoPoint, distance: number): Observable<Organisation[]> {
        return this.http
            .post('api/organisation/byQuestionAnswersAndPosition',
                answers,
                {
                    params: {
                        position: GeoPoint.asString(position),
                        distance
                    }
                })
            .map((response: Response) => response.json());
    }

    boundingBox(position: GeoPoint, distance: number, boundingBox: BoundingBox, zoom: number): Observable<GeoPoint[]> {
        return this.http
            .post('api/organisation/boundingBox', {
                position,
                distance,
                boundingBox,
                zoom
            })
            .map((response: Response) => response.json());
    }

    getOrganisation(urlName: string): Observable<Organisation> {
        return this.http.get('api/organisation/' + urlName)
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

export class Organisation {
    public id: string;
    public name: string;
    public urlName: string;
    public description: string;
    public website: string;
    public scoreNorm: number;
    public mapPin: string;
    public contactPersons: ContactPerson[] = [];
    public defaultAddress: Address;
    public addresses: Address[] = [];
    public groups: Group[] = [];
    public questions: AnsweredQuestion[] = [];
    public logo: PictureId;
    public volunteers: Volunteer[] = [];
    public attendanceTimes: AttendanceTime[] = [];
}

export class PictureId {
    public value: string;
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

export class UserAnswer {
    public id: string;
    public answer: Answer;
}

export class Volunteer {
    public firstname: string;
    public motivation: string;
    public picture: PictureId;
}

export class BoundingBox {
    public northEast: GeoPoint;
    public southWest: GeoPoint;


    constructor(northEast: GeoPoint, southWest: GeoPoint) {
        this.northEast = northEast;
        this.southWest = southWest;
    }
}

export class ContactPerson {
    public firstname: string;
    public lastname: string;
    public rank: string;
    public telephone: string;
    public mail: string;
    public picture: PictureId;
}

export class Address {
    public street: string;
    public addressAppendix: string;
    public city: string;
    public zipcode: string;
    public location: GeoPoint;
    public telephone: string;
    public website: string;
}