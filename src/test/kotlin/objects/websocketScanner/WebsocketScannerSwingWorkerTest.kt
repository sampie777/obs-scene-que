package objects.websocketScanner

import java.beans.PropertyChangeEvent
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class WebsocketScannerSwingWorkerTest {

    @Test
    fun testOnPropertyChangeCallsProcessStatusMethod() {
        val mock = WebsocketScannerFrameMock()
        val worker = WebsocketScannerSwingWorker(mock)

        val event = PropertyChangeEvent(this, WebsocketScannerProcessStatus.STATUS_PROGRESS, null, "newValue")
        worker.propertyChange(event)

        assertTrue(mock.isProcessScanStatusCalled)
        assertEquals("newValue", mock.processScanStatusValue)
    }

    @Test
    fun testOnPropertyChangeCallsAddValueMethod() {
        val mock = WebsocketScannerFrameMock()
        val worker = WebsocketScannerSwingWorker(mock)

        val event = PropertyChangeEvent(this, WebsocketScannerProcessStatus.VALUE_PROGRESS, null, ScanResult("result1", 123, true))
        worker.propertyChange(event)

        assertTrue(mock.isAddScanResultCalled)
        assertEquals("result1", mock.addScanResultValue!!.ip)
    }

    @Test
    fun testOnPropertyChangeCallsNoMethodOnDiffertentChange() {
        val mock = WebsocketScannerFrameMock()
        val worker = WebsocketScannerSwingWorker(mock)

        val event = PropertyChangeEvent(this, "someproperty", null, "value")
        worker.propertyChange(event)

        assertFalse(mock.isProcessScanStatusCalled)
        assertFalse(mock.isAddScanResultCalled)
    }
}