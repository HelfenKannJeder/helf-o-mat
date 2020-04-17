package de.helfenkannjeder.helfomat.core.organization;

/**
 * @author Valentin Zickner
 */
public class ScoredOrganization {

    private final Organization organization;

    private final float score;

    public ScoredOrganization(Organization organization, float score) {
        this.organization = organization;
        this.score = score;
    }

    public Organization getOrganization() {
        return organization;
    }

    public float getScore() {
        return score;
    }

}
