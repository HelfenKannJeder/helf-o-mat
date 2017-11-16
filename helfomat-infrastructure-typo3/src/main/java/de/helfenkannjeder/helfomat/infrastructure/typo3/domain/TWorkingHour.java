package de.helfenkannjeder.helfomat.infrastructure.typo3.domain;

import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.List;

/**
 * @author Valentin Zickner
 */
@Entity(name = "tx_helfenkannjeder_domain_model_workinghour")
public class TWorkingHour {

    @Id
    private long uid;

    private int day;

    private int starttimehour;

    private int stoptimehour;

    private int starttimeminute;

    private int stoptimeminute;

    private String addition;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "tx_helfenkannjeder_workinghour_group_mm",
        joinColumns = {@JoinColumn(name = "uid_local")},
        inverseJoinColumns = {@JoinColumn(name = "uid_foreign")}
    )
    @Where(clause = "deleted=0")
    private List<TGroup> groups;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getStarttimehour() {
        return starttimehour;
    }

    public void setStarttimehour(int starttimehour) {
        this.starttimehour = starttimehour;
    }

    public int getStoptimehour() {
        return stoptimehour;
    }

    public void setStoptimehour(int stoptimehour) {
        this.stoptimehour = stoptimehour;
    }

    public int getStarttimeminute() {
        return starttimeminute;
    }

    public void setStarttimeminute(int starttimeminute) {
        this.starttimeminute = starttimeminute;
    }

    public int getStoptimeminute() {
        return stoptimeminute;
    }

    public void setStoptimeminute(int stoptimeminute) {
        this.stoptimeminute = stoptimeminute;
    }

    public String getAddition() {
        return addition;
    }

    public void setAddition(String addition) {
        this.addition = addition;
    }

    public List<TGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<TGroup> groups) {
        this.groups = groups;
    }
}
