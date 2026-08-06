package de.helfenkannjeder.helfomat.api.contact

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class MailboxKeyTest {

    @Test
    fun treatsGmailAddressesThatDifferOnlyInDotsAsOneMailbox() {
        // The addresses seen in the spam submissions, all delivering to mwfujinami@gmail.com
        assertThat("m.w.fuji.nam.i@gmail.com".toMailboxKey())
            .isEqualTo("mwfujinami@gmail.com")
            .isEqualTo("mw.fujinami@gmail.com".toMailboxKey())
            .isEqualTo("mwfujinami@gmail.com".toMailboxKey())
    }

    @Test
    fun ignoresEverythingAfterAPlusInTheLocalPart() {
        assertThat("mwfujinami+helfomat@gmail.com".toMailboxKey()).isEqualTo("mwfujinami@gmail.com")
    }

    @Test
    fun treatsGooglemailAsGmail() {
        assertThat("m.w.fuji.nam.i@googlemail.com".toMailboxKey()).isEqualTo("mwfujinami@googlemail.com")
    }

    @Test
    fun keepsDotsForProvidersThatDistinguishThem() {
        assertThat("d.abramaitys@att.net".toMailboxKey()).isEqualTo("d.abramaitys@att.net")
        assertThat("dabramaitys@att.net".toMailboxKey()).isEqualTo("dabramaitys@att.net")
    }

    @Test
    fun normalizesCaseAndSurroundingWhitespace() {
        assertThat("  Max.Mustermann@Example.COM ".toMailboxKey()).isEqualTo("max.mustermann@example.com")
    }

    @Test
    fun leavesInputWithoutAUsableDomainAlone() {
        assertThat("not-an-address".toMailboxKey()).isEqualTo("not-an-address")
        assertThat("@example.com".toMailboxKey()).isEqualTo("@example.com")
        assertThat("local@".toMailboxKey()).isEqualTo("local@")
    }

}
