package de.helfenkannjeder.helfomat.core.organization

/**
 * @author Valentin Zickner
 */
class IllegalOrganizationTypeException(invalidName: String)
    : RuntimeException("Unable to find organization type $invalidName")