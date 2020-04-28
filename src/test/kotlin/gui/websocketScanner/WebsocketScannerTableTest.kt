package gui.websocketScanner

import objects.websocketScanner.ScanResult
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class WebsocketScannerTableTest {

    @Test
    fun testClearTableClearsTable() {
        val panel = WebsocketScannerTable()
        panel.addScanResult(ScanResult("", 0, true))
        panel.addScanResult(ScanResult("", 0, true))
        assertEquals(2, panel.model().rowCount)

        panel.clearTable()

        assertEquals(0, panel.model().rowCount)
        assertTrue(panel.model().dataVector.isEmpty())
    }

    @Test
    fun testAddScanResultAddsRowToTable() {
        val panel = WebsocketScannerTable()
        assertEquals(0, panel.model().rowCount)
        assertTrue(panel.model().dataVector.isEmpty())

        panel.addScanResult(ScanResult("1", 0, true))
        panel.addScanResult(ScanResult("2", 0, true))

        assertEquals(2, panel.model().rowCount)
        assertEquals(2, panel.model().dataVector.size)
        assertEquals("1", (panel.model().dataVector[0] as Vector<*>)[1])
        assertEquals("2", (panel.model().dataVector[1] as Vector<*>)[1])
    }

    @Test
    fun testSetScanResultsClearsTableAndAddsRowsToTable() {
        val panel = WebsocketScannerTable()
        panel.addScanResult(ScanResult("I will be cleared", 0, true))
        assertEquals(1, panel.model().rowCount)
        assertEquals(1, panel.model().dataVector.size)

        panel.setScanResults(
            listOf(
                ScanResult("1", 0, true),
                ScanResult("2", 0, true)
            )
        )

        assertEquals(2, panel.model().rowCount)
        assertEquals(2, panel.model().dataVector.size)
        assertEquals("1", (panel.model().dataVector[0] as Vector<*>)[1])
        assertEquals("2", (panel.model().dataVector[1] as Vector<*>)[1])
    }

    @Test
    fun testGetSelectedValueAsAddressGivesCorrectAddressOfSelectedRow() {
        val panel = WebsocketScannerTable()
        panel.addScanResult(ScanResult("address1", 111, true))
        panel.addScanResult(ScanResult("address2", 234, true, "Result 2"))

        panel.table.setRowSelectionInterval(0, 0)
        assertEquals("ws://address1:111", panel.getSelectedValueAsAddress())

        panel.table.setRowSelectionInterval(1, 1)
        assertEquals("ws://address2:234", panel.getSelectedValueAsAddress())
    }

    @Test
    fun testGetSelectedValueAsAddressGivesNullOnEmptySelection() {
        val panel = WebsocketScannerTable()
        panel.addScanResult(ScanResult("address1", 111, true))
        panel.addScanResult(ScanResult("address2", 234, true, "Result 2"))

        panel.table.removeRowSelectionInterval(0, 1)

        assertNull(panel.getSelectedValueAsAddress())
    }

    @Test
    fun testGetSelectedValueAsAddressGivesNullOnInvalidSelectionBound() {
        val panel = WebsocketScannerTable()
        panel.addScanResult(ScanResult("address1", 111, true))

        panel.table.setRowSelectionInterval(0, 0)
        panel.clearTable()

        assertNull(panel.getSelectedValueAsAddress())
    }

    @Test
    fun testGetSelectedValueAsAddressGivesNullOnInvalidSelectionVector() {
        val panel = WebsocketScannerTable()
        panel.addScanResult(ScanResult("address1", 111, true))
        panel.addScanResult(ScanResult("address2", 234, true, "Result 2"))
        panel.table.setRowSelectionInterval(0, 0)

        (panel.model().dataVector[0] as Vector<*>).clear()

        assertEquals(0, (panel.model().dataVector[0] as Vector<*>).size)
        assertNull(panel.getSelectedValueAsAddress())
    }
}