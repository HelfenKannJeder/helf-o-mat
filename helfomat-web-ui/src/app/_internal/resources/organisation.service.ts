import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {Answer} from '../../shared/answer.model';
import {GeoPoint} from '../../../_internal/geopoint';
import {HttpClient} from "@angular/common/http";

@Injectable()
export class OrganisationService {

    constructor(private httpClient: HttpClient) {
    }

    findGlobal(): Observable<Organisation[]> {
        return this.httpClient.get<Organisation[]>('api/organisation/global');
    }

    findGlobalByQuestionAnswers(answers: UserAnswer[]): Observable<Organisation[]> {
        return this.httpClient.post<Organisation[]>('api/organisation/global/byQuestionAnswers', answers);
    }

    findByPosition(position: GeoPoint, distance: number): Observable<Organisation[]> {
        return this.httpClient.get<Organisation[]>('api/organisation/byPosition', {
            params: {
                position: GeoPoint.asString(position),
                distance: String(distance)
            }
        });
    }

    findByQuestionAnswersAndPosition(answers: UserAnswer[], position: GeoPoint, distance: number): Observable<Organisation[]> {
        return this.httpClient.post<Organisation[]>('api/organisation/byQuestionAnswersAndPosition',
            answers,
            {
                params: {
                    position: GeoPoint.asString(position),
                    distance: String(distance)
                }
            });
    }

    boundingBox(position: GeoPoint, distance: number, boundingBox: BoundingBox, zoom: number): Observable<GeoPoint[]> {
        return this.httpClient.post<GeoPoint[]>('api/organisation/boundingBox', {
            position,
            distance,
            boundingBox,
            zoom
        });
    }

    getOrganisation(urlName: string): Observable<Organisation> {
        return this.httpClient.get<Organisation>('api/organisation/' + urlName);
    }

    getTravelDistances(id: string, location: GeoPoint): Observable<Array<TravelDistance>> {
        return this.httpClient.get<Array<TravelDistance>>(`api/organisation/${id}/travelDistances`, {
            params: {
                lat: String(location.lat),
                lon: String(location.lon)
            }
        });
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

export class AnsweredQuestion {
    public question: string;
    public answer: string;
    public description: string;
    public id: string;
    public position: number;
}


export class AttendanceTime {
    public day: DayOfWeek;
    public start: number[];
    public end: number[];
    public note: string;
    public groups: Array<Group>;
}

export enum DayOfWeek {
    MONDAY = "MONDAY",
    TUESDAY = "TUESDAY",
    WEDNESDAY = "WEDNESDAY",
    THURSDAY = "THURSDAY",
    FRIDAY = "FRIDAY",
    SATURDAY = "SATURDAY",
    SUNDAY = "SUNDAY"
}

export class Group {
    public name: string;
    public description: string;
}