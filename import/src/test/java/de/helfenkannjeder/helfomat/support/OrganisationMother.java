package de.helfenkannjeder.helfomat.support;

import de.helfenkannjeder.helfomat.domain.*;

public class OrganisationMother {
    public static OrganisationBuilder anyOrganisation() {
        return new OrganisationBuilder().setId("1").setType("anyType");
    }
}
