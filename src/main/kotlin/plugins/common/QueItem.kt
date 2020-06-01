package plugins.common

import objects.que.JsonQue
import objects.que.Que
import themes.Theme
import java.awt.Color
import javax.swing.BorderFactory
import javax.swing.JLabel
import javax.swing.border.CompoundBorder
import javax.swing.border.EmptyBorder

interface QueItem {
    val name: String
    val plugin: BasePlugin

    var executeAfterPrevious: Boolean

    fun renderText(): String = name

    fun activateAsPrevious() = activate()
    fun activate()
    fun deactivateAsPrevious() = deactivate()
    fun deactivate()

    @Deprecated(
        "Use JSON converter instead of this plane text converter",
        ReplaceWith("toJson()", "")
    )
    fun toConfigString(): String
    fun toJson(): JsonQue.QueItem = JsonQue.QueItem(
        pluginName = plugin.name,
        className = javaClass.simpleName,
        name = name,
        executeAfterPrevious = executeAfterPrevious,
        data = HashMap()
    )

    fun dataFromJson(jsonQueItem: JsonQue.QueItem) {
        executeAfterPrevious = jsonQueItem.executeAfterPrevious
    }

    fun getListCellRendererComponent(
        cell: JLabel,
        index: Int,
        isSelected: Boolean,
        cellHasFocus: Boolean
    ) {
        cell.text = renderText()
        cell.icon = plugin.icon

        if (!executeAfterPrevious) {
            cell.border = EmptyBorder(1, 10, 1, 10)
        } else {
            cell.border = EmptyBorder(1, 25, 1, 10)
        }

        if (name != Que.current()?.name || index != Que.currentIndex()) {
            return
        }

        cell.background = if (isSelected) Theme.get.ACTIVE_QUE_AND_OBS_SELECTED_COLOR
        else Theme.get.ACTIVE_QUE_AND_OBS_COLOR

        cell.border = CompoundBorder(
            BorderFactory.createLineBorder(Color(85, 194, 85)),
            cell.border
        )
    }
}