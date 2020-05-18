package gui.websocketScanner

import config.Config
import objects.websocketScanner.ScanResult
import objects.websocketScanner.WebsocketScannerSwingWorker
import java.awt.BorderLayout
import java.util.logging.Logger
import javax.swing.JDialog
import javax.swing.JFrame
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.border.EmptyBorder

open class WebsocketScannerFrame(private val parentFrame: JFrame?, private val visible: Boolean = true) : JDialog(parentFrame) {
    private val logger = Logger.getLogger(WebsocketScannerFrame::class.java.name)

    val websocketScannerTable = WebsocketScannerTable()
    private val websocketScannerStatusPanel = WebsocketScannerStatusPanel()
    private lateinit var websocketScannerActionPanel: WebsocketScannerActionPanel
    private var worker: WebsocketScannerSwingWorker? = null

    init {
        createGui()
    }

    private fun createGui() {
        websocketScannerActionPanel = WebsocketScannerActionPanel(this)

        val mainPanel = JPanel(BorderLayout(10, 10))
        mainPanel.border = EmptyBorder(10, 10, 10, 10)
        add(mainPanel)

        val bottomPanel = JPanel(BorderLayout())
        bottomPanel.add(websocketScannerStatusPanel, BorderLayout.LINE_START)
        bottomPanel.add(websocketScannerActionPanel, BorderLayout.LINE_END)

        mainPanel.add(WebsocketScannerInfoPanel(), BorderLayout.PAGE_START)
        mainPanel.add(websocketScannerTable, BorderLayout.CENTER)
        mainPanel.add(bottomPanel, BorderLayout.PAGE_END)

        title = "Network Scanner"
        setSize(600, 520)
        setLocationRelativeTo(parentFrame)
        modalityType = ModalityType.APPLICATION_MODAL
        isVisible = visible
    }

    fun scan(timeout: Int) {
        websocketScannerActionPanel.buttonsEnable(false)

        websocketScannerTable.clearTable()

        worker = WebsocketScannerSwingWorker(this, timeout)
        worker!!.execute()
    }

    open fun processScanStatus(status: String) {
        websocketScannerStatusPanel.updateStatus(status)
    }

    fun processScanResults(scanResults: List<ScanResult>) {
        websocketScannerTable.setScanResults(scanResults)
    }

    open fun addScanResult(scanResult: ScanResult) {
        websocketScannerTable.addScanResult(scanResult)
    }

    fun onScanFinished() {
        websocketScannerActionPanel.buttonsEnable(true)
    }

    fun close() {
        if (worker != null && !worker!!.isDone) {
            worker!!.cancel(true)   // Although canceling doesn't do very much because it is not handled in WebsocketScanner
        }

        dispose()
    }

    fun save(): Boolean {
        val newAddress = websocketScannerTable.getSelectedValueAsAddress()
        if (newAddress.isNullOrEmpty()) {
            logger.info("No valid value selected")
            if (parentFrame != null) {
                JOptionPane.showMessageDialog(
                    this,
                    "No value selected to save. Please select a table row and try\n" +
                            "Save again, or use Cancel to exit this window.",
                    "No selection",
                    JOptionPane.WARNING_MESSAGE
                )
            }
            return false
        }

        logger.info("Saving new obsAddress: $newAddress")
        Config.obsAddress = newAddress
        return true
    }
}