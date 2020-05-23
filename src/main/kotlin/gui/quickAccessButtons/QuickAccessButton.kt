package gui.quickAccessButtons

import com.google.gson.Gson
import config.Config
import gui.utils.isCtrlClick
import handles.QueItemDropComponent
import handles.QueItemTransferHandler
import objects.notifications.Notifications
import plugins.common.QueItem
import themes.Theme
import java.awt.Dimension
import java.awt.event.ActionEvent
import java.util.logging.Logger
import javax.swing.JButton

class QuickAccessButton(
    private val index: Int,
    private var queItem: QueItem? = null
) : JButton(), QueItemDropComponent {

    private val logger = Logger.getLogger(QuickAccessButton::class.java.name)

    init {
        setSize(100, 100)
        minimumSize = Dimension(100, 100)
        preferredSize = Dimension(100, 100)
        transferHandler = QueItemTransferHandler()
        addActionListener { e -> onClick(e) }

        refreshLayout()
    }

    override fun dropNewItem(item: QueItem, index: Int): Boolean {
        setNewItem(item)
        return true
    }

    override fun dropMoveItem(item: QueItem, fromIndex: Int, toIndex: Int): Boolean {
        setNewItem(item)
        return true
    }

    private fun setNewItem(item: QueItem?) {
        queItem = item
        refreshLayout()

        if (item == null) {
            Config.quickAccessButtonQueItems[index] = ""
        } else {
            Config.quickAccessButtonQueItems[this.index] = Gson().toJson(item.toJson())
        }
    }

    private fun refreshLayout() {
        if (queItem == null) {
            text = "empty"
            background = null
            isEnabled = false
            return
        }

        text = "<html><center>${queItem?.name}</center></html>"
        background = Theme.get.BUTTON_BACKGROUND_COLOR
        isEnabled = true
    }

    fun getQueItem(): QueItem? = queItem

    private fun onClick(e: ActionEvent) {
        if (isCtrlClick(e.modifiers)) {
            clearItem()
        } else {
            activate()
        }
    }

    fun clearItem() {
        setNewItem(null)
    }

    private fun activate() {
        if (queItem == null) {
            logger.info("No quick access item specified to activate")
            return
        }

        try {
            queItem!!.activate()
        } catch (e: Exception) {
            logger.warning("Failed to activate quick access item: $queItem")
            e.printStackTrace()
            Notifications.add("Failed to activate '${queItem?.name}'", "QuickAccessButton")
        }
    }
}