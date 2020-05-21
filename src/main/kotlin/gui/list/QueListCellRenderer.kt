package gui.list

import brightness
import plugins.common.QueItem
import themes.Theme
import java.awt.Component
import javax.swing.DefaultListCellRenderer
import javax.swing.JLabel
import javax.swing.JList

class QueListCellRenderer : DefaultListCellRenderer() {

    override fun getListCellRendererComponent(
        list: JList<*>,
        value: Any?,
        index: Int,
        isSelected: Boolean,
        cellHasFocus: Boolean
    ): Component {
        val cell = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus) as JLabel
        if (value == null) {
            return cell
        }

        val item = value as QueItem
        item.getListCellRendererComponent(cell, index, isSelected, cellHasFocus)

        if (brightness(cell.background) > 110) {
            cell.foreground = Theme.get.LIST_SELECTION_FONT_COLOR_DARK
        } else {
            cell.foreground = Theme.get.LIST_SELECTION_FONT_COLOR_LIGHT
        }

        return cell
    }
}