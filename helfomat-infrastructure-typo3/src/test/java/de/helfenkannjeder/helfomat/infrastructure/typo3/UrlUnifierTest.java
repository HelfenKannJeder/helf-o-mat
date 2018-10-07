package de.helfenkannjeder.helfomat.infrastructure.typo3;

import org.junit.Test;

import static de.helfenkannjeder.helfomat.infrastructure.typo3.UrlUnifier.unifyOrganisationWebsiteUrl;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class UrlUnifierTest {

    @Test
    public void unifyOrganisationWebsiteUrl_withNull_returnNull() {
        assertThat(unifyOrganisationWebsiteUrl(null), is(nullValue()));
    }

    @Test
    public void unifyOrganisationWebsiteUrl_withEmptyString_returnNull() {
        assertThat(unifyOrganisationWebsiteUrl(""), is(nullValue()));
    }

    @Test
    public void urlStartingWithWwwIsCompletedWithHttp() {
        assertThat(unifyOrganisationWebsiteUrl("www.test.de"), is("http://www.test.de"));
    }

    @Test
    public void urlStartingWithhttpIsCunchanged() {
        assertThat(unifyOrganisationWebsiteUrl("http://www.test.de"), is("http://www.test.de"));
    }

    @Test
    public void urlStartingWithhttpsIsCunchanged() {
        assertThat(unifyOrganisationWebsiteUrl("https://www.test.de"), is("https://www.test.de"));
    }

    @Test
    public void closingShlashIsRemoved() {
        assertThat(unifyOrganisationWebsiteUrl("https://www.test.de/"), is("https://www.test.de"));
    }

    @Test
    public void unifyOrganisationWebsiteUrl_withWhichIsStartingWithHttp_returnHttpBeforeUrl() {
        assertThat(unifyOrganisationWebsiteUrl("http.helfenkannjeder.de/"), is("http://http.helfenkannjeder.de"));
    }
}