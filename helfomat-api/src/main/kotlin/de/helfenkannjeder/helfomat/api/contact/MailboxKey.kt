package de.helfenkannjeder.helfomat.api.contact

private val DOT_INSENSITIVE_DOMAINS = setOf("gmail.com", "googlemail.com")

/**
 * Reduces an address to the mailbox it actually reaches, so that one mailbox cannot be counted as
 * many different ones.
 *
 * Gmail ignores dots in the local part and everything from a '+' onwards, which means
 * `m.w.fuji.nam.i@gmail.com`, `mw.fujinami@gmail.com` and `mwfujinami+x@gmail.com` are all the same
 * inbox. Spam submissions use exactly those variants to look like a new address on every attempt,
 * so rate limiting on the raw input would never trigger.
 */
fun String.toMailboxKey(): String {
    val address = trim().lowercase()
    val separator = address.lastIndexOf('@')
    if (separator <= 0 || separator == address.length - 1) {
        return address
    }

    val domain = address.substring(separator + 1)
    var localPart = address.substring(0, separator).substringBefore('+')
    if (domain in DOT_INSENSITIVE_DOMAINS) {
        localPart = localPart.replace(".", "")
    }

    return "$localPart@$domain"
}
