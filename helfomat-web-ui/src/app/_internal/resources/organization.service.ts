import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {Answer} from '../../shared/answer.model';
import {GeoPoint} from '../../../_internal/geopoint';
import {HttpClient} from "@angular/common/http";

@Injectable()
export class OrganizationService {

    constructor(private httpClient: HttpClient) {
    }

    findGlobal(): Observable<Organization[]> {
        return this.httpClient.get<Organization[]>('api/organization/global');
    }

    findGlobalByQuestionAnswers(answers: UserAnswer[]): Observable<Organization[]> {
        return this.httpClient.post<Organization[]>('api/organization/global/byQuestionAnswers', answers);
    }

    findByPosition(position: GeoPoint, distance: number): Observable<Organization[]> {
        return this.httpClient.get<Organization[]>('api/organization/byPosition', {
            params: {
                position: GeoPoint.asString(position),
                distance: String(distance)
            }
        });
    }

    findByQuestionAnswersAndPosition(answers: UserAnswer[], position: GeoPoint, distance: number): Observable<Organization[]> {
        return this.httpClient.post<Organization[]>('api/organization/byQuestionAnswersAndPosition',
            answers,
            {
                params: {
                    position: GeoPoint.asString(position),
                    distance: String(distance)
                }
            });
    }

    boundingBox(position: GeoPoint, distance: number, boundingBox: BoundingBox, zoom: number): Observable<GeoPoint[]> {
        return this.httpClient.post<GeoPoint[]>('api/organization/boundingBox', {
            position,
            distance,
            boundingBox,
            zoom
        });
    }

    getOrganization(urlName: string): Observable<Organization> {
        return this.httpClient.get<Organization>('api/organization/' + urlName);
    }

    getTravelDistances(id: string, location: GeoPoint): Observable<Array<TravelDistance>> {
        return this.httpClient.get<Array<TravelDistance>>(`api/organization/${id}/travelDistances`, {
            params: {
                lat: String(location.lat),
                lon: String(location.lon)
            }
        });
    }

    compareOrganization(original: Organization, updated: Organization): Observable<Array<OrganizationEvent>> {
        return this.httpClient.post<Array<OrganizationEvent>>('api/organization/compare', {
            original,
            updated
        });
    }

    submitOrganizationEvents(organizationId: OrganizationId, sources: string, events: OrganizationEvent[]): Observable<void> {
        return this.httpClient.post<void>('api/organization/submit', {
            organizationId,
            sources,
            events
        });
    }
}

export class Organization {
    public id: string;
    public name: string;
    public urlName: string;
    public organizationType: string;
    public description: string;
    public website: string;
    public scoreNorm: number;
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

    static isEqual(address1: Address, address2: Address): boolean {
        return address1.location.lon == address2.location.lon
            && address1.location.lat == address2.location.lat
            && address1.addressAppendix == address2.addressAppendix
            && address1.city == address2.city
            && address1.street == address2.street
            && address1.telephone == address2.telephone
            && address1.zipcode == address2.zipcode
            && address1.website == address2.website;
    }
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

export class OrganizationEvent {
    public organizationId: OrganizationId;
    public type: string;
}

export class OrganizationId {
    public value: string;
}
