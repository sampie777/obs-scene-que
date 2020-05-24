package mocks

import plugins.common.DetailPanelBasePlugin
import javax.swing.Icon
import javax.swing.JComponent
import javax.swing.JPanel

class MockPluginDetailPanel : DetailPanelBasePlugin {
    override val name: String = "MockPluginDetailPanel"
    override val description: String = "description"
    override val version: String = "0.0.0"
    override val icon: Icon? = null
    override val tabName: String = "MockPluginDetailPanelTabName"

    override fun detailPanel(): JComponent {
        return JPanel()
    }
}