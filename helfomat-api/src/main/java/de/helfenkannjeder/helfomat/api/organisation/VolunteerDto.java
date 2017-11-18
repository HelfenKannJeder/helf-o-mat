package de.helfenkannjeder.helfomat.api.organisation;

import de.helfenkannjeder.helfomat.core.picture.PictureId;

/**
 * @author Valentin Zickner
 */
public class VolunteerDto {
    private String firstname;
    private String motivation;
    private PictureId picture;

    VolunteerDto(String firstname, String motivation, PictureId picture) {
        this.firstname = firstname;
        this.motivation = motivation;
        this.picture = picture;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getMotivation() {
        return motivation;
    }

    public PictureId getPicture() {
        return picture;
    }
}
