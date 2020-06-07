package de.helfenkannjeder.helfomat.api

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.context.SecurityContextHolder


object AuthenticationUtil {
    fun clearAuthentication() {
        SecurityContextHolder.getContext().authentication = null
    }

    fun configureAuthentication(role: String?) {
        val authorities: Collection<GrantedAuthority> = AuthorityUtils.createAuthorityList(role)
        val authentication: Authentication = UsernamePasswordAuthenticationToken(
            "technical-user",
            role,
            authorities
        )
        SecurityContextHolder.getContext().authentication = authentication
    }
}