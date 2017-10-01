package de.helfenkannjeder.helfomat.typo3;

public class UrlUnifier {
    public static String unifyOrganisationWebsiteUrl(String urlToImport) {
        if (urlToImport == null || urlToImport.equals("")) {
            return null;
        }

        // add missing http/https
        if (!urlToImport.matches("^(http://|https://).*")) {
            urlToImport = "http://" + urlToImport;
        }

        // remove closing slashes
        if (urlToImport.endsWith("/")) {
            urlToImport = urlToImport.substring(0, urlToImport.length() - 1);
        }

        return urlToImport;
    }
}
