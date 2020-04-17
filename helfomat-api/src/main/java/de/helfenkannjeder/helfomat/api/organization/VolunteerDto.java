package de.helfenkannjeder.helfomat.api.organization;

import de.helfenkannjeder.helfomat.core.picture.PictureId;

/**
 * @author Valentin Zickner
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class VolunteerDto {
    private String firstname;
    private String motivation;
    private PictureId picture;

    private VolunteerDto() {
    }

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
