package de.helfenkannjeder.helfomat.infrastructure.typo3

import de.helfenkannjeder.helfomat.infrastructure.typo3.UrlUnifier.unifyOrganizationWebsiteUrl
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.jupiter.api.Test

class UrlUnifierTest {

    @Test
    fun unifyOrganizationWebsiteUrl_withNull_returnNull() {
        MatcherAssert.assertThat(unifyOrganizationWebsiteUrl(null), `is`(CoreMatchers.nullValue()))
    }

    @Test
    fun unifyOrganizationWebsiteUrl_withEmptyString_returnNull() {
        MatcherAssert.assertThat(unifyOrganizationWebsiteUrl(""), `is`(CoreMatchers.nullValue()))
    }

    @Test
    fun urlStartingWithWwwIsCompletedWithHttp() {
        MatcherAssert.assertThat(unifyOrganizationWebsiteUrl("www.test.de"), `is`("http://www.test.de"))
    }

    @Test
    fun urlStartingWithhttpIsCunchanged() {
        MatcherAssert.assertThat(unifyOrganizationWebsiteUrl("http://www.test.de"), `is`("http://www.test.de"))
    }

    @Test
    fun urlStartingWithhttpsIsCunchanged() {
        MatcherAssert.assertThat(unifyOrganizationWebsiteUrl("https://www.test.de"), `is`("https://www.test.de"))
    }

    @Test
    fun closingShlashIsRemoved() {
        MatcherAssert.assertThat(unifyOrganizationWebsiteUrl("https://www.test.de/"), `is`("https://www.test.de"))
    }

    @Test
    fun unifyOrganizationWebsiteUrl_withWhichIsStartingWithHttp_returnHttpBeforeUrl() {
        MatcherAssert.assertThat(unifyOrganizationWebsiteUrl("http.helfenkannjeder.de/"), `is`("http://http.helfenkannjeder.de"))
    }

}