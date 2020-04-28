package objects.websocketScanner

import java.beans.PropertyChangeListener
import java.beans.PropertyChangeSupport

class WebsocketScannerProcessStatus(p0: PropertyChangeListener) : PropertyChangeSupport(this) {
    companion object {
        const val STATUS_PROGRESS = "WebsocketScannerProcessStatus"
        const val VALUE_PROGRESS = "WebsocketScannerProcessValue"
    }

    private var state: String = "Not initialized"

    init {
        addPropertyChangeListener(p0)
    }

    fun setState(newState: String) {
        firePropertyChange(STATUS_PROGRESS, state, newState)
        state = newState
    }

    fun addScanResult(value: ScanResult) {
        firePropertyChange(VALUE_PROGRESS, null, value)
    }
}