package objects.que

import gui.list.QueListCellRenderer
import plugins.common.QueItemBasePlugin
import themes.Theme
import java.awt.Color
import java.awt.Graphics2D
import javax.swing.BorderFactory
import javax.swing.Icon
import javax.swing.JLabel
import javax.swing.border.CompoundBorder
import javax.swing.border.EmptyBorder

interface QueItem {
    val name: String
    val plugin: QueItemBasePlugin

    var executeAfterPrevious: Boolean
    var quickAccessColor: Color?
    val icon: Icon?
        get() = null

    fun renderText(): String = name

    fun activateAsPrevious() = activate()
    fun activate()
    fun deactivateAsPrevious() = deactivate()
    fun deactivate() {}

    fun clone(): QueItem {
        val jsonQueItem = toJson()
        val newItem = plugin.jsonToQueItem(jsonQueItem)
        newItem.dataFromJson(jsonQueItem)
        return newItem
    }

    @Deprecated(
        "Use JSON converter instead of this plane text converter",
        ReplaceWith("toJson()", "")
    )
    fun toConfigString(): String {
        throw NotImplementedError("This method is deprecated. Please use the latest version of the application.")
    }

    fun toJson(): JsonQueue.QueueItem = JsonQueue.QueueItem(
        pluginName = plugin.name,
        className = javaClass.simpleName,
        name = name,
        executeAfterPrevious = executeAfterPrevious,
        quickAccessColor = quickAccessColor,
        data = HashMap()
    )

    fun dataFromJson(jsonQueueItem: JsonQueue.QueueItem) {
        executeAfterPrevious = jsonQueueItem.executeAfterPrevious
        quickAccessColor = jsonQueueItem.quickAccessColor
    }

    fun getListCellRendererComponent(
        cell: JLabel,
        index: Int,
        isSelected: Boolean,
        cellHasFocus: Boolean
    ) {
        cell.text = renderText()
        cell.icon = icon ?: plugin.icon

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

    fun listCellRendererPaintAction(g: Graphics2D, queListCellRenderer: QueListCellRenderer) {}
}