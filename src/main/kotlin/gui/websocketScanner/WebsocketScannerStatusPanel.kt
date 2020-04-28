package gui.websocketScanner

import java.awt.Font
import javax.swing.JLabel
import javax.swing.JPanel

class WebsocketScannerStatusPanel : JPanel() {

    private val statusLabel = JLabel()

    init {
        createGui()
    }

    private fun createGui() {
        statusLabel.font = Font("Dialog", Font.PLAIN, 12)
        add(statusLabel)
    }

    fun updateStatus(status: String) {
        statusLabel.text = status
    }
}
