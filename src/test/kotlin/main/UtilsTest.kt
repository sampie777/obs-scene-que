package main

import getTimeAsClock
import isAddressLocalhost
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UtilsTest {

    @Test
    fun testGetTimeAsClock() {
        assertEquals("0:00:00", getTimeAsClock(0))
        assertEquals("0:00:01", getTimeAsClock(1))
        assertEquals("0:00:10", getTimeAsClock(10))
        assertEquals("0:00:59", getTimeAsClock(59))
        assertEquals("0:01:00", getTimeAsClock(60))
        assertEquals("0:01:01", getTimeAsClock(61))
        assertEquals("0:10:01", getTimeAsClock(601))
        assertEquals("0:59:59", getTimeAsClock(3599))
        assertEquals("1:00:00", getTimeAsClock(3600))
        assertEquals("1:00:01", getTimeAsClock(3601))
        assertEquals("1:01:01", getTimeAsClock(3661))
        assertEquals("10:00:00", getTimeAsClock(36000))
        assertEquals("10:00:01", getTimeAsClock(36001))
    }

    @Test
    fun testIsAddressLocalhost() {
        assertTrue(isAddressLocalhost("ws://localhost:4444"))
        assertTrue(isAddressLocalhost("ws://127.0.0.1:4444"))
        assertTrue(isAddressLocalhost("http://localhost/"))
        assertFalse(isAddressLocalhost("ws://196.178.168.1:4444"))
        assertFalse(isAddressLocalhost("ws://somedomain:4444"))
        assertFalse(isAddressLocalhost("http://somedomain/"))
    }
}