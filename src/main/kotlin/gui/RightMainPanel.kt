package gui

import GUI
import config.Config
import objects.notifications.Notifications
import plugins.PluginLoader
import plugins.common.DetailPanelBasePlugin
import java.awt.BorderLayout
import java.awt.Component
import java.util.logging.Logger
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JSplitPane

class RightMainPanel : JPanel(), Refreshable {

    private val logger = Logger.getLogger(RightMainPanel::class.java.name)

    private val splitPanel = JSplitPane(JSplitPane.VERTICAL_SPLIT)

    init {
        initGui()

        GUI.register(this)
    }

    private fun initGui() {
        layout = BorderLayout(10, 10)

        val detailPanelPlugin = getDetailPanelPlugin()

        val detailPanel = try {
            detailPanelPlugin?.detailPanel()
        } catch (e: Exception) {
            logger.warning("Failed to load detail panel from plugin: ${detailPanelPlugin?.name}")
            e.printStackTrace()
            Notifications.add("Failed to load detail panel from plugin: ${detailPanelPlugin?.name}", "Plugins")
            null
        }

        if (detailPanel == null) {
            add(MainPanelControlPanel(), BorderLayout.CENTER)
            return
        }

        detailPanel.toolTipText = "${detailPanelPlugin!!.name} [${detailPanelPlugin.version}]"

        val topScrollPane = JScrollPane(MainPanelControlPanel())
        topScrollPane.border = null
        val bottomScrollPane = JScrollPane(detailPanel)
        bottomScrollPane.border = null

        splitPanel.border = null
        splitPanel.topComponent = topScrollPane
        splitPanel.bottomComponent = bottomScrollPane
        splitPanel.dividerLocation = 300
        add(splitPanel, BorderLayout.CENTER)

        if (Config.windowRestoreLastPosition) {
            splitPanel.dividerLocation = Config.rightMainPanelDividerLocation
        }
    }

    override fun windowClosing(window: Component?) {
        Config.rightMainPanelDividerLocation = splitPanel.dividerLocation
        GUI.unregister(this)
    }

    private fun getDetailPanelPlugin(): DetailPanelBasePlugin? {
        return PluginLoader.detailPanelPlugins.firstOrNull()
    }
}