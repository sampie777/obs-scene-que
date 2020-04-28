package gui.websocketScanner

import objects.websocketScanner.ScanResult
import java.awt.BorderLayout
import java.util.*
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTable
import javax.swing.ListSelectionModel
import javax.swing.table.DefaultTableModel

class WebsocketScannerTable : JPanel() {

    private val tableHeader = arrayOf("Name", "Address", "Port")
    val table = JTable(ReadOnlyTableModel(tableHeader, 0))

    init {
        createGui()
    }

    private fun createGui() {
        layout = BorderLayout()

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION)

        add(JScrollPane(table), BorderLayout.CENTER)
    }

    fun clearTable() {
        model().rowCount = 0
    }

    fun model(): DefaultTableModel = table.model as DefaultTableModel

    fun setScanResults(scanResults: List<ScanResult>) {
        clearTable()

        scanResults.forEach {
            addScanResult(it)
        }
    }

    fun addScanResult(scanResult: ScanResult) {
        model().addRow(arrayOf(scanResult.hostName, scanResult.ip, scanResult.port))
    }

    fun getSelectedValueAsAddress(): String? {
        val row: Vector<*>?
        try {
            row = model().dataVector[table.selectedRow] as? Vector<*>
        } catch (e: ArrayIndexOutOfBoundsException) {
            return null
        }

        if (row == null || row.size < 3) {
            return null
        }

        return "ws://${row[1]}:${row[2]}"
    }
}