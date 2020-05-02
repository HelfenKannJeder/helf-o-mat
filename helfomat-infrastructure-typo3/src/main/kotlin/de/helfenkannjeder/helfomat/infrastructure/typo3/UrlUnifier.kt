package de.helfenkannjeder.helfomat.infrastructure.typo3

internal object UrlUnifier {
    @JvmStatic
    fun unifyOrganizationWebsiteUrl(urlToImport: String?): String? {
        var urlToImport = urlToImport
        if (urlToImport == null || urlToImport == "") {
            return null
        }

        // add missing http/https
        if (!urlToImport.matches(Regex("^(http://|https://).*"))) {
            urlToImport = "http://$urlToImport"
        }

        // remove closing slashes
        if (urlToImport.endsWith("/")) {
            urlToImport = urlToImport.substring(0, urlToImport.length - 1)
        }
        return urlToImport
    }
}