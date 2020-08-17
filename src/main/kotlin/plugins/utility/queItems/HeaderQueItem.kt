package plugins.utility.queItems

import objects.que.Que
import objects.que.QueItem
import plugins.utility.TextQueItemsCompanion
import plugins.utility.UtilityPlugin
import themes.Theme
import java.awt.Color
import java.awt.Font
import javax.swing.BorderFactory
import javax.swing.Icon
import javax.swing.JLabel
import javax.swing.border.CompoundBorder
import javax.swing.border.EmptyBorder

class HeaderQueItem(override val plugin: UtilityPlugin, override val name: String) : QueItem {

    override var executeAfterPrevious = false
    override var quickAccessColor: Color? = plugin.quickAccessColor
    override val icon: Icon? = HeaderQueItem.icon

    companion object QueItemCompanion : TextQueItemsCompanion(HeaderQueItem::class.java, "Header")

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