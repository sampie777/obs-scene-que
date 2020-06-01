package mocks

import objects.que.JsonQue
import objects.que.QueItem
import plugins.common.QueItemBasePlugin
import javax.swing.Icon
import javax.swing.JComponent
import javax.swing.JMenu
import javax.swing.JPanel

class MockPlugin2 : QueItemBasePlugin {
    override val name: String = "MockPlugin2"
    override val description: String = "description"
    override val version: String = "0.0.0"
    override val icon: Icon? = null
    override val tabName: String = "MockPlugin2TabName"

    override fun sourcePanel(): JComponent {
        return JPanel()
    }

    override fun configStringToQueItem(value: String): QueItem {
        return QueItemMock(this, value)
    }

    override fun jsonToQueItem(jsonQueItem: JsonQue.QueItem): QueItem {
        return QueItemMock(this, jsonQueItem.name)
    }

    override fun createMenu(menu: JMenu): Boolean {
        throw Exception("Woops")
    }
}