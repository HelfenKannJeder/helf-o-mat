package de.helfenkannjeder.helfomat.core.organization;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

/**
 * @author Valentin Zickner
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal", "unused"})
public class AttendanceTime {
    private DayOfWeek day;
    private LocalTime start;
    private LocalTime end;
    private String note;
    private List<Group> groups;

    protected AttendanceTime() {
    }

    private AttendanceTime(DayOfWeek day, LocalTime start, LocalTime end, String note, List<Group> groups) {
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

    public List<Group> getGroups() {
        return groups;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttendanceTime that = (AttendanceTime) o;
        return day == that.day &&
            Objects.equals(start, that.start) &&
            Objects.equals(end, that.end) &&
            Objects.equals(note, that.note) &&
            Objects.equals(groups, that.groups);
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, start, end, note, groups);
    }

    public static class Builder {
        private DayOfWeek day;
        private LocalTime start;
        private LocalTime end;
        private String note;
        private List<Group> groups;

        public Builder setDay(DayOfWeek day) {
            this.day = day;
            return this;
        }

        public Builder setStart(LocalTime start) {
            this.start = start;
            return this;
        }

        public Builder setEnd(LocalTime end) {
            this.end = end;
            return this;
        }

        public Builder setNote(String note) {
            this.note = note;
            return this;
        }

        public Builder setGroups(List<Group> groups) {
            this.groups = groups;
            return this;
        }

        public AttendanceTime build() {
            return new AttendanceTime(day, start, end, note, groups);
        }
    }

}