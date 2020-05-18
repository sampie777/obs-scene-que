package objects.websocketScanner

import gui.websocketScanner.WebsocketScannerFrame
import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener
import java.util.logging.Logger
import javax.swing.SwingWorker

class WebsocketScannerSwingWorker(
    private val component: WebsocketScannerFrame,
    private val timeout: Int = 200
) : SwingWorker<Boolean, Void>(),
    PropertyChangeListener {
    private val logger = Logger.getLogger(WebsocketScannerSwingWorker::class.java.name)

    override fun doInBackground(): Boolean {
        val processStatus = WebsocketScannerProcessStatus(this)
        val websocketScanner = WebsocketScanner(processStatus, timeout)
        websocketScanner.scan()

        component.processScanStatus("Scan finished")
        component.onScanFinished()
        return true
    }

    override fun propertyChange(event: PropertyChangeEvent?) {
        if (event == null) {
            return
        }

        if (WebsocketScannerProcessStatus.STATUS_PROGRESS == event.propertyName) {
            component.processScanStatus(event.newValue as String)
        }

        if (WebsocketScannerProcessStatus.VALUE_PROGRESS == event.propertyName) {
            component.addScanResult(event.newValue as ScanResult)
        }
    }
}