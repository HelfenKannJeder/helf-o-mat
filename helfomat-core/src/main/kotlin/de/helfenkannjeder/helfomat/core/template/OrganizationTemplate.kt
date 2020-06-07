package de.helfenkannjeder.helfomat.core.template

import de.helfenkannjeder.helfomat.core.picture.PictureId

/**
 * @author Valentin Zickner
 */
data class OrganizationTemplate(
    val name: String,
    val acronym: String,
    val logoSuggestions: List<PictureId>,
    val isEligibleForRegistration: Boolean,
    val groups: List<GroupTemplate>
)