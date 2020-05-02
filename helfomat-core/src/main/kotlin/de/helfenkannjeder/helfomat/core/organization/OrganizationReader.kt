package de.helfenkannjeder.helfomat.core.organization

/**
 * @author Valentin Zickner
 */
interface OrganizationReader {
    val name: String

    fun read(): Organization?
}