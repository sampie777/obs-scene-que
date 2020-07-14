package mocks

import objects.que.JsonQueue
import objects.que.QueItem
import plugins.common.QueItemBasePlugin
import javax.swing.Icon
import javax.swing.JComponent
import javax.swing.JMenu
import javax.swing.JPanel

class MockPlugin3 : QueItemBasePlugin {
    override val name: String = "MockPlugin3"
    override val description: String = "description"
    override val version: String = "0.0.0"
    override val icon: Icon? = null
    override val tabName: String = "MockPlugin3TabName"

    override fun sourcePanel(): JComponent {
        return JPanel()
    }

    override fun configStringToQueItem(value: String): QueItem {
        return QueItemMock(this, value)
    }

    override fun jsonToQueItem(jsonQueueItem: JsonQueue.QueueItem): QueItem {
        return QueItemMock(this, jsonQueueItem.name)
    }

    override fun createMenu(menu: JMenu): Boolean {
        super.createMenu(menu)
        return true
    }
}