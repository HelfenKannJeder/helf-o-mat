package de.helfenkannjeder.helfomat.core.geopoint

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class GeoPointTest {

    @Test
    fun testEquals_withDifferenceIn10thLatAfterDecimalPoint_ensureEqualsIsTrue() {
        val point1 = GeoPoint(49.1234567890, 8.1234567890)
        val point2 = GeoPoint(49.12345678901, 8.1234567890)

        assertThat(point1.equals(point2)).isTrue()
        assertThat(point1.hashCode()).isEqualTo(point2.hashCode())
    }

    @Test
    fun testEquals_withDifferenceIn9thLatAfterDecimalPoint_ensureEqualsIsFalse() {
        val point1 = GeoPoint(49.1234567890, 8.1234567890)
        val point2 = GeoPoint(49.1234567891, 8.1234567890)

        assertThat(point1.equals(point2)).isFalse()
        assertThat(point1.hashCode()).isNotEqualTo(point2.hashCode())
    }

    @Test
    fun testEquals_withDifferenceIn10thLonAfterDecimalPoint_ensureEqualsIsTrue() {
        val point1 = GeoPoint(49.1234567890, 8.1234567890)
        val point2 = GeoPoint(49.1234567890, 8.12345678901)

        assertThat(point1.equals(point2)).isTrue()
        assertThat(point1.hashCode()).isEqualTo(point2.hashCode())
    }

    @Test
    fun testEquals_withDifferenceIn9thLonAfterDecimalPoint_ensureEqualsIsFalse() {
        val point1 = GeoPoint(49.1234567890, 8.1234567890)
        val point2 = GeoPoint(49.1234567890, 8.1234567891)

        assertThat(point1.equals(point2)).isFalse()
        assertThat(point1.hashCode()).isNotEqualTo(point2.hashCode())
    }

}