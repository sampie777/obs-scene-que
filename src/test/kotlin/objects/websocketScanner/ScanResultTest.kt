package objects.websocketScanner

import kotlin.test.Test
import kotlin.test.assertEquals

class ScanResultTest {

    @Test
    fun testToString() {
        assertEquals("address1:123 (hostName1)", ScanResult("address1", 123, true, "hostName1").toString())
        assertEquals("address1:123 (null)", ScanResult("address1", 123, true, null).toString())
    }
}