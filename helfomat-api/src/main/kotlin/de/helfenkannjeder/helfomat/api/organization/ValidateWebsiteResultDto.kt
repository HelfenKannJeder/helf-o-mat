package de.helfenkannjeder.helfomat.api.organization

data class ValidateWebsiteResultDto (
    val resultUrl: String,
    val reachable: Boolean
)