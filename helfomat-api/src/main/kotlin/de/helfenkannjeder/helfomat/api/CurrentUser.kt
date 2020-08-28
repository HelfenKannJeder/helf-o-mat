package de.helfenkannjeder.helfomat.api

import org.springframework.security.core.context.SecurityContextHolder

fun isAuthenticated() = !currentUserHasAnyRole(Roles.ANONYMOUS)
fun currentUsername() = SecurityContextHolder.getContext().authentication.name
fun currentUserHasAnyRole(vararg roles: String) = roles.any { role -> SecurityContextHolder.getContext().authentication.authorities.any { it.authority == role } }