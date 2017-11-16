package de.helfenkannjeder.helfomat.core.organisation;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

/**
 * @author Valentin Zickner
 */
public class Event {
    private DayOfWeek day;
    private LocalTime start;
    private LocalTime end;
    private String note;
    private List<Group> groups;

    protected Event() {
    }

    private Event(DayOfWeek day, LocalTime start, LocalTime end, String note, List<Group> groups) {
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

        public Event build() {
            return new Event(day, start, end, note, groups);
        }
    }

}
