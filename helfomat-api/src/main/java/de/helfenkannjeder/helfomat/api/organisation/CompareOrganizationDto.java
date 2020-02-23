package de.helfenkannjeder.helfomat.api.organisation;

public class CompareOrganizationDto {
    private OrganisationDetailDto original;
    private OrganisationDetailDto updated;

    private CompareOrganizationDto() {
    }

    public CompareOrganizationDto(OrganisationDetailDto original, OrganisationDetailDto updated) {
        this.original = original;
        this.updated = updated;
    }

    public OrganisationDetailDto getOriginal() {
        return original;
    }

    public OrganisationDetailDto getUpdated() {
        return updated;
    }
}
