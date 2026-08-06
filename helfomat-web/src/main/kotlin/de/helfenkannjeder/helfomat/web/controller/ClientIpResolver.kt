package de.helfenkannjeder.helfomat.web.controller

import javax.servlet.http.HttpServletRequest

/**
 * Works out which address a request came from, for the checks that count submissions per client.
 *
 * Deployed, the application sits behind an ingress, so the socket address is the proxy and the
 * client is only visible in X-Forwarded-For. That header is a list and the caller can put anything
 * at the front of it, but the proxy appends the address it actually saw, so the last entry is the
 * one that can be trusted. Without a proxy in front the header is absent and the socket address is
 * already the client.
 */
object ClientIpResolver {

    private const val FORWARDED_FOR_HEADER = "X-Forwarded-For"
    private const val UNKNOWN = "unknown"

    fun resolve(request: HttpServletRequest): String =
        request.getHeader(FORWARDED_FOR_HEADER)
            ?.split(",")
            ?.lastOrNull()
            ?.trim()
            ?.takeIf { it.isNotEmpty() }
            ?: request.remoteAddr
            ?: UNKNOWN

}
