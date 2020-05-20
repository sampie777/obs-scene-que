package mocks

import plugins.common.BasePlugin
import plugins.common.QueItem
import javax.swing.Icon
import javax.swing.JComponent
import javax.swing.JPanel

class MockPlugin : BasePlugin {
    override val name: String = "MockPlugin"
    override val description: String = "description"
    override val version: String = "0.0.0"
    override val icon: Icon? = null
    override val tabName: String = "MockPluginTabName"

    override fun sourcePanel(): JComponent {
        return JPanel()
    }

    override fun configStringToQueItem(value: String): QueItem {
        return QueItemMock(this, value)
    }
}