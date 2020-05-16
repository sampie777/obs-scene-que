package plugins.common

import objects.que.Que
import themes.Theme
import javax.swing.JLabel

interface QueItem {
    val name: String
    val plugin: BasePlugin

    fun activate()
    fun deactivate()

    fun toConfigString(): String
    fun getListCellRendererComponent(
        cell: JLabel,
        index: Int,
        isSelected: Boolean,
        cellHasFocus: Boolean
    ) {
        cell.text = name
        cell.icon = plugin.icon

        if (name == Que.current()?.name && index == Que.currentIndex()) {
            cell.background = if (isSelected) Theme.get.ACTIVE_QUE_AND_OBS_SELECTED_COLOR
            else Theme.get.ACTIVE_QUE_AND_OBS_COLOR
        }
    }
}