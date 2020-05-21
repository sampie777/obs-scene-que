package gui.list

import brightness
import plugins.common.QueItem
import themes.Theme
import java.awt.*
import javax.swing.DefaultListCellRenderer
import javax.swing.JList

class QueListCellRenderer : DefaultListCellRenderer() {

    var item: QueItem? = null

    override fun getListCellRendererComponent(
        list: JList<*>,
        value: Any?,
        index: Int,
        isSelected: Boolean,
        cellHasFocus: Boolean
    ): Component {
        val cell = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus) as QueListCellRenderer
        if (value == null) {
            return cell
        }

        cell.item = value as QueItem
        cell.item!!.getListCellRendererComponent(cell, index, isSelected, cellHasFocus)

        if (brightness(cell.background) > 110) {
            cell.foreground = Theme.get.LIST_SELECTION_FONT_COLOR_DARK
        } else {
            cell.foreground = Theme.get.LIST_SELECTION_FONT_COLOR_LIGHT
        }

        return cell
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)

        if (item != null && item!!.executeAfterPrevious) {
            paintExecuteAfterPrevious(g as Graphics2D)
        }
    }

    private fun paintExecuteAfterPrevious(g2: Graphics2D) {
        g2.color = Color(180, 180, 180)
        g2.stroke = BasicStroke(1F)

        val leftPadding = 16
        val horizontalLineWidth = 5
        g2.drawLine(leftPadding, 0, leftPadding, height / 2)
        g2.drawLine(leftPadding, height / 2, leftPadding + horizontalLineWidth, height / 2)
    }
}