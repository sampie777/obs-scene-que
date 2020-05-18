package mocks

import plugins.common.BasePlugin
import plugins.common.QueItem
import javax.swing.Icon
import javax.swing.JComponent
import javax.swing.JPanel

class MockPlugin2 : BasePlugin {
    override val name: String = "MockPlugin2"
    override val description: String = "description"
    override val icon: Icon? = null
    override val tabName: String = "MockPlugin2TabName"

    override fun sourcePanel(): JComponent {
        return JPanel()
    }

    override fun configStringToQueItem(value: String): QueItem {
        return QueItemMock(this, value)
    }
}