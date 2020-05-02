package de.helfenkannjeder.helfomat.core.organization

/**
 * @author Valentin Zickner
 */
data class Group(
    var name: String,
    var description: String?,
    var contactPersons: List<ContactPerson> = emptyList(),
    var minimumAge: Int? = null,
    var maximumAge: Int? = null,
    var website: String? = null
)