package gui.websocketScanner

import config.Config
import java.awt.Dimension
import java.util.logging.Logger
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.border.EmptyBorder

class WebsocketScannerActionPanel(private val frame: WebsocketScannerFrame) : JPanel() {
    private val logger = Logger.getLogger(WebsocketScannerActionPanel::class.java.name)

    private val buttonsToEnable = ArrayList<JButton>()

    init {
        createGui()
        buttonsEnable(true)
    }

    private fun createGui() {
        layout = BoxLayout(this, BoxLayout.LINE_AXIS)
        border = EmptyBorder(0, 10, 10, 10)

        val scanButton = JButton("Scan")
        buttonsToEnable.add(scanButton)
        scanButton.addActionListener { frame.scan() }

        val saveButton = JButton("Save")
        saveButton.addActionListener { saveConfigAndClose() }

        val cancelButton = JButton("Cancel")
        cancelButton.addActionListener { cancelWindow() }

        add(Box.createHorizontalGlue())
        add(scanButton)
        add(Box.createRigidArea(Dimension(10, 0)))
        add(saveButton)
        add(Box.createRigidArea(Dimension(10, 0)))
        add(cancelButton)
    }

    private fun cancelWindow() {
        logger.info("Exiting websocket scanner window")
        frame.close()
    }

    private fun saveConfigAndClose() {
        logger.info("Saving websocket address changes")
        if (!frame.save()) {
            return
        }

        Config.save()
        frame.close()
    }

    fun buttonsEnable(enable: Boolean) {
        buttonsToEnable.forEach { it.isEnabled = enable }
    }
}
