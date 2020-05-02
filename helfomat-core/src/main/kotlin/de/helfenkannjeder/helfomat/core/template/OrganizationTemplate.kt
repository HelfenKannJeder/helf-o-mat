package de.helfenkannjeder.helfomat.core.template

/**
 * @author Valentin Zickner
 */
data class OrganizationTemplate(
    val name: String,
    val acronym: String,
    val isEligibleForRegistration: Boolean,
    val groups: List<GroupTemplate>
)