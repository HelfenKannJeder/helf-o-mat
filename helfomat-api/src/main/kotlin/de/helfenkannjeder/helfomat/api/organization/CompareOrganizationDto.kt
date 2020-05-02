package de.helfenkannjeder.helfomat.api.organization;

public class CompareOrganizationDto {
    private OrganizationDetailDto original;
    private OrganizationDetailDto updated;

    private CompareOrganizationDto() {
    }

    public CompareOrganizationDto(OrganizationDetailDto original, OrganizationDetailDto updated) {
        this.original = original;
        this.updated = updated;
    }

    public OrganizationDetailDto getOriginal() {
        return original;
    }

    public OrganizationDetailDto getUpdated() {
        return updated;
    }
}
