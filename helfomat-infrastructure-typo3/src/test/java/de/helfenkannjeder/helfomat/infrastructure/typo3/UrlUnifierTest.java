package de.helfenkannjeder.helfomat.infrastructure.typo3;

import org.junit.Test;

import static de.helfenkannjeder.helfomat.infrastructure.typo3.UrlUnifier.unifyOrganizationWebsiteUrl;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class UrlUnifierTest {

    @Test
    public void unifyOrganizationWebsiteUrl_withNull_returnNull() {
        assertThat(unifyOrganizationWebsiteUrl(null), is(nullValue()));
    }

    @Test
    public void unifyOrganizationWebsiteUrl_withEmptyString_returnNull() {
        assertThat(unifyOrganizationWebsiteUrl(""), is(nullValue()));
    }

    @Test
    public void urlStartingWithWwwIsCompletedWithHttp() {
        assertThat(unifyOrganizationWebsiteUrl("www.test.de"), is("http://www.test.de"));
    }

    @Test
    public void urlStartingWithhttpIsCunchanged() {
        assertThat(unifyOrganizationWebsiteUrl("http://www.test.de"), is("http://www.test.de"));
    }

    @Test
    public void urlStartingWithhttpsIsCunchanged() {
        assertThat(unifyOrganizationWebsiteUrl("https://www.test.de"), is("https://www.test.de"));
    }

    @Test
    public void closingShlashIsRemoved() {
        assertThat(unifyOrganizationWebsiteUrl("https://www.test.de/"), is("https://www.test.de"));
    }

    @Test
    public void unifyOrganizationWebsiteUrl_withWhichIsStartingWithHttp_returnHttpBeforeUrl() {
        assertThat(unifyOrganizationWebsiteUrl("http.helfenkannjeder.de/"), is("http://http.helfenkannjeder.de"));
    }
}