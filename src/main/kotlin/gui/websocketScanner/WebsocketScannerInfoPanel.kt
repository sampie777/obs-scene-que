package gui.websocketScanner

import java.awt.Font
import javax.swing.BoxLayout
import javax.swing.JLabel
import javax.swing.JPanel

class WebsocketScannerInfoPanel : JPanel() {

    init {
        createGui()
    }

    private fun createGui() {
        layout = BoxLayout(this, BoxLayout.PAGE_AXIS)

        val titleLabel = JLabel("<html>Websocket scanner<html>")
        titleLabel.font = Font("Dialog", Font.BOLD, 12)

        val infoLabel = JLabel("<html>Click scan to scan the local network for any open OBS websockets. " +
                "The scanner will scan all available IP addresses matching 192.168.*.* on port 4444.</html>")
        infoLabel.font = Font("Dialog", Font.PLAIN, 12)

        add(titleLabel)
        add(infoLabel)
    }
}
