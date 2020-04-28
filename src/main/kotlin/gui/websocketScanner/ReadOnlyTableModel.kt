package gui.websocketScanner

import javax.swing.table.DefaultTableModel

class ReadOnlyTableModel(headers: Array<String>, columnCount: Int) : DefaultTableModel(headers, columnCount) {
    override fun isCellEditable(p0: Int, p1: Int): Boolean = false
}