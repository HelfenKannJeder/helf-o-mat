package de.helfenkannjeder.helfomat.core.organisation;

/**
 * @author Valentin Zickner
 */
public class ScoredOrganisation {

    private final Organisation organisation;

    private final float score;

    public ScoredOrganisation(Organisation organisation, float score) {
        this.organisation = organisation;
        this.score = score;
    }

    public Organisation getOrganisation() {
        return organisation;
    }

    public float getScore() {
        return score;
    }

}
