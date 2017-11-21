import {Group} from './group.model';

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