package de.helfenkannjeder.helfomat.typo3.domain;

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
@Entity(name = "tx_helfenkannjeder_domain_model_groupofgrouptemplate")
public class TGroupOfGroupTemplate {
    @Id
    private long uid;

    private String name;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "tx_helfenkannjeder_helfomatquestion_positive_gogt_mm",
            joinColumns = @JoinColumn(name = "uid_foreign", referencedColumnName = "uid"),
            inverseJoinColumns = @JoinColumn(name = "uid_local", referencedColumnName = "uid"))
    @Where(clause="deleted=0")
    private List<TQuestion> positive;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "tx_helfenkannjeder_helfomatquestion_positive_not_gogt_mm",
            joinColumns = @JoinColumn(name = "uid_foreign", referencedColumnName = "uid"),
            inverseJoinColumns = @JoinColumn(name = "uid_local", referencedColumnName = "uid"))
    @Where(clause="deleted=0")
    private List<TQuestion> negativeNot;


    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TQuestion> getPositive() {
        return positive;
    }

    public void setPositive(List<TQuestion> positive) {
        this.positive = positive;
    }

    public List<TQuestion> getNegativeNot() {
        return negativeNot;
    }

    public void setNegativeNot(List<TQuestion> negativeNot) {
        this.negativeNot = negativeNot;
    }

    @Override
    public String toString() {
        return "TGroupOfGroupTemplate{" +
                "uid=" + uid +
                ", name='" + name + '\'' +
                ", positive=" + positive +
                ", negativeNot=" + negativeNot +
                '}';
    }
}
