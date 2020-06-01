package plugins.text.queItems

import objects.que.Que
import plugins.common.QueItem
import plugins.text.TextPlugin
import plugins.text.TextPluginQueItemCompanion
import themes.Theme
import java.awt.Color
import java.awt.Font
import javax.swing.BorderFactory
import javax.swing.JLabel
import javax.swing.border.CompoundBorder
import javax.swing.border.EmptyBorder

class HeaderQueItem(override val plugin: TextPlugin, override val name: String) : QueItem {

    override var executeAfterPrevious = false

    companion object QueItemCompanion : TextPluginQueItemCompanion(HeaderQueItem::class.java, "Header");

    override fun activateAsPrevious() {
        // Skip this queue item by going to the previous one
        Que.previous()
    }

    override fun activate() {
        // Skip this queue item by going to the next one
        Que.next()
    }

    override fun deactivate() {}

    override fun toConfigString(): String = javaClass.simpleName + plugin.configStringSeparator + name

    override fun getListCellRendererComponent(cell: JLabel, index: Int, isSelected: Boolean, cellHasFocus: Boolean) {
        super.getListCellRendererComponent(cell, index, isSelected = false, cellHasFocus = false)
        cell.border = CompoundBorder(
            CompoundBorder(
                EmptyBorder(10, 1, 0, 0),
                BorderFactory.createMatteBorder(2, 0, 0, 0, Color(180, 180, 180))
            ),
            EmptyBorder(2, 0, 2, 0)
        )
        cell.font = Font(Theme.get.FONT_FAMILY, Font.BOLD, cell.font.size)
    }

}