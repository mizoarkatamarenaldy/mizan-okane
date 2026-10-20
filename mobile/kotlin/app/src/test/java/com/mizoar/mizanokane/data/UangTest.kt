package com.mizoar.mizanokane.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

/**
 * Unit test JVM untuk helper konversi [Uang].
 */
class UangTest {

    @Test
    fun testUangDariJsonContent() {
        assertEquals(4990L, Uang.dariJsonContent("49.9"))
        assertEquals(4990L, Uang.dariJsonContent("49.90"))
        assertEquals(1299L, Uang.dariJsonContent("12.99"))
        assertEquals(10000L, Uang.dariJsonContent("100"))
        assertEquals(10000L, Uang.dariJsonContent("100.00"))
        assertEquals(1L, Uang.dariJsonContent("0.01"))
        assertEquals(0L, Uang.dariJsonContent("0"))
        assertEquals(-500000L, Uang.dariJsonContent("-5000"))
        assertEquals(150000000L, Uang.dariJsonContent("1500000"))
    }

    @Test
    fun testUangKePlainString() {
        // Bilangan bulat tanpa titik desimal (100, bukan 100.0)
        assertEquals("100", Uang.kePlainString(10000L))
        assertEquals("15000", Uang.kePlainString(1500000L))
        assertEquals("0", Uang.kePlainString(0L))
        assertEquals("-5000", Uang.kePlainString(-500000L))

        // Pecahan tanpa nol di belakang (49.9, bukan 49.90)
        assertEquals("49.9", Uang.kePlainString(4990L))
        assertEquals("12.99", Uang.kePlainString(1299L))
        assertEquals("0.01", Uang.kePlainString(1L))
    }

    @Test
    fun testAngkaTigaDesimalDitolak() {
        assertThrows(IllegalArgumentException::class.java) {
            Uang.dariJsonContent("49.999")
        }
        assertThrows(IllegalArgumentException::class.java) {
            Uang.dariJsonContent("0.001")
        }
        assertThrows(IllegalArgumentException::class.java) {
            Uang.dariJsonContent("12.345")
        }
    }
}
