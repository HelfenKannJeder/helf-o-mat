package de.helfenkannjeder.helfomat.infrastructure.typo3;

import org.junit.jupiter.api.Test;

import static de.helfenkannjeder.helfomat.infrastructure.typo3.UrlUnifier.unifyOrganizationWebsiteUrl;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class UrlUnifierTest {

    @Test
    void unifyOrganizationWebsiteUrl_withNull_returnNull() {
        assertThat(unifyOrganizationWebsiteUrl(null), is(nullValue()));
    }

    @Test
    void unifyOrganizationWebsiteUrl_withEmptyString_returnNull() {
        assertThat(unifyOrganizationWebsiteUrl(""), is(nullValue()));
    }

    @Test
    void urlStartingWithWwwIsCompletedWithHttp() {
        assertThat(unifyOrganizationWebsiteUrl("www.test.de"), is("http://www.test.de"));
    }

    @Test
    void urlStartingWithhttpIsCunchanged() {
        assertThat(unifyOrganizationWebsiteUrl("http://www.test.de"), is("http://www.test.de"));
    }

    @Test
    void urlStartingWithhttpsIsCunchanged() {
        assertThat(unifyOrganizationWebsiteUrl("https://www.test.de"), is("https://www.test.de"));
    }

    @Test
    void closingShlashIsRemoved() {
        assertThat(unifyOrganizationWebsiteUrl("https://www.test.de/"), is("https://www.test.de"));
    }

    @Test
    void unifyOrganizationWebsiteUrl_withWhichIsStartingWithHttp_returnHttpBeforeUrl() {
        assertThat(unifyOrganizationWebsiteUrl("http.helfenkannjeder.de/"), is("http://http.helfenkannjeder.de"));
    }
}