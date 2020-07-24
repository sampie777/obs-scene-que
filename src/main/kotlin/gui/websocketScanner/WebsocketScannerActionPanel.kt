package gui.websocketScanner

import config.Config
import themes.Theme
import java.awt.Dimension
import java.awt.Font
import java.awt.event.KeyEvent
import java.util.logging.Logger
import javax.swing.*
import javax.swing.border.EmptyBorder

class WebsocketScannerActionPanel(private val frame: WebsocketScannerFrame) : JPanel() {
    private val logger = Logger.getLogger(WebsocketScannerActionPanel::class.java.name)

    private val buttonsToEnable = ArrayList<JButton>()
    private val timeoutSpinner = JSpinner()

    init {
        createGui()
        buttonsEnable(true)
    }

    private fun createGui() {
        layout = BoxLayout(this, BoxLayout.LINE_AXIS)
        border = EmptyBorder(0, 10, 10, 10)

        val timeoutLabel = JLabel("Timeout (ms): ")
        timeoutLabel.font = Font(Theme.get.FONT_FAMILY, Font.PLAIN, 10)

        timeoutSpinner.model = SpinnerNumberModel(200, 1, Int.MAX_VALUE, 50)
        timeoutSpinner.preferredSize = Dimension(60, 18)
        timeoutSpinner.toolTipText = "Adjust this value to increase the port scanning timeout if you are on a slow network"
        timeoutSpinner.font = timeoutLabel.font

        val scanButton = JButton("Scan")
        buttonsToEnable.add(scanButton)
        scanButton.addActionListener { frame.scan(timeoutSpinner.value as Int) }
        scanButton.mnemonic = KeyEvent.VK_S

        val saveButton = JButton("Save")
        saveButton.addActionListener { saveConfigAndClose() }
        frame.rootPane.defaultButton = saveButton

        val cancelButton = JButton("Cancel")
        cancelButton.addActionListener { cancelWindow() }
        cancelButton.mnemonic = KeyEvent.VK_C

        add(Box.createHorizontalGlue())
        add(timeoutLabel)
        add(timeoutSpinner)
        add(Box.createRigidArea(Dimension(10, 0)))
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
