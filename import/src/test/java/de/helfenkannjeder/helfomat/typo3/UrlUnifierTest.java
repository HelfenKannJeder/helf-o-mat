package de.helfenkannjeder.helfomat.typo3;

import org.junit.Test;

import static de.helfenkannjeder.helfomat.typo3.UrlUnifier.unifyOrganisationWebsiteUrl;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class UrlUnifierTest {

    @Test
    public void urlStartingWithWwwIsCompletedWithHttp() throws Exception {
        assertThat(unifyOrganisationWebsiteUrl("www.test.de"), is("http://www.test.de"));
    }

    @Test
    public void urlStartingWithhttpIsCunchanged() throws Exception {
        assertThat(unifyOrganisationWebsiteUrl("http://www.test.de"), is("http://www.test.de"));
    }

    @Test
    public void urlStartingWithhttpsIsCunchanged() throws Exception {
        assertThat(unifyOrganisationWebsiteUrl("https://www.test.de"), is("https://www.test.de"));
    }

    @Test
    public void closingShlashIsRemoved() throws Exception {
        assertThat(unifyOrganisationWebsiteUrl("https://www.test.de/"), is("https://www.test.de"));
    }
}