package de.helfenkannjeder.helfomat.api.organisation;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

/**
 * @author Valentin Zickner
 */
public class AttendanceTimeDto {
    private DayOfWeek day;
    private LocalTime start;
    private LocalTime end;
    private String note;
    private List<GroupDto> groups;

    public AttendanceTimeDto(DayOfWeek day, LocalTime start, LocalTime end, String note, List<GroupDto> groups) {
        this.day = day;
        this.start = start;
        this.end = end;
        this.note = note;
        this.groups = groups;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public LocalTime getStart() {
        return start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public String getNote() {
        return note;
    }

    public List<GroupDto> getGroups() {
        return groups;
    }
}
