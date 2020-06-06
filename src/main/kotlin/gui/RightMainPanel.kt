package gui

import GUI
import config.Config
import objects.notifications.Notifications
import plugins.PluginLoader
import java.awt.BorderLayout
import java.awt.Component
import java.awt.Dimension
import java.util.logging.Logger
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JSplitPane
import javax.swing.JTabbedPane

class RightMainPanel : JPanel(), Refreshable {

    private val logger = Logger.getLogger(RightMainPanel::class.java.name)

    private val splitPanel = JSplitPane(JSplitPane.VERTICAL_SPLIT)
    private var tabbedPane: JTabbedPane? = null

    init {
        initGui()

        GUI.register(this)
    }

    private fun initGui() {
        layout = BorderLayout(10, 10)

        when (PluginLoader.detailPanelPlugins.size) {
            0 -> {
                add(MainPanelControlPanel(), BorderLayout.CENTER)
                return
            }
            1 -> splitPanel.bottomComponent = getDetailPanel()
            else -> splitPanel.bottomComponent = getDetailTabbedPanel()
        }

        splitPanel.topComponent = MainPanelControlPanel()
        splitPanel.topComponent.minimumSize = Dimension(10, 10)
        splitPanel.bottomComponent.minimumSize = Dimension(10, 10)

        splitPanel.border = null
        splitPanel.dividerLocation = 300
        add(splitPanel, BorderLayout.CENTER)

        if (Config.windowRestoreLastPosition) {
            splitPanel.dividerLocation = Config.rightMainPanelDividerLocation
        }
    }

    override fun windowClosing(window: Component?) {
        Config.rightMainPanelDividerLocation = splitPanel.dividerLocation
        if (tabbedPane != null) {
            Config.detailPanelLastOpenedTab = tabbedPane!!.getTitleAt(tabbedPane!!.selectedIndex)
        }

        GUI.unregister(this)
    }

    private fun getDetailPanel(): JComponent {
        val detailPanelPlugin = PluginLoader.detailPanelPlugins.first()

        return try {
            val detailPanel = detailPanelPlugin.detailPanel()
            detailPanel.toolTipText = "${detailPanelPlugin.name} [${detailPanelPlugin.version}]"
            detailPanel
        } catch (e: AbstractMethodError) {
            logger.warning("Failed to load detail panel for plugin: ${detailPanelPlugin.name}")
            e.printStackTrace()
            Notifications.add("Failed to load detail panel for plugin: ${detailPanelPlugin.name}", "Plugins")
            JPanel()
        } catch (e: Exception) {
            logger.warning("Failed to load detail panel for plugin: ${detailPanelPlugin.name}")
            e.printStackTrace()
            Notifications.add("Failed to load detail panel for plugin: ${detailPanelPlugin.name}", "Plugins")
            JPanel()
        }
    }

    private fun getDetailTabbedPanel(): JTabbedPane {
        tabbedPane = JTabbedPane()
        tabbedPane!!.tabPlacement = JTabbedPane.TOP
        tabbedPane!!.border = null

        PluginLoader.detailPanelPlugins
            .sortedBy { it.name }
            .forEach {
                val tabComponent: JComponent
                try {
                    tabComponent = it.detailPanel()
                    tabComponent.toolTipText = "${it.name} [${it.version}]"

                    tabbedPane!!.addTab(
                        it.tabName,
                        it.icon,
                        tabComponent,
                        "${it.description} [${it.version}]"
                    )
                } catch (e: AbstractMethodError) {
                    logger.warning("Failed to load detail panel for plugin: ${it.name}")
                    e.printStackTrace()
                    Notifications.add("Failed to load detail panel for plugin: ${it.name}", "Plugins")
                    return@forEach
                } catch (e: Exception) {
                    logger.warning("Failed to load detail panel for plugin: ${it.name}")
                    e.printStackTrace()
                    Notifications.add("Failed to load detail panel for plugin: ${it.name}", "Plugins")
                    return@forEach
                }

                if (it.tabName == Config.detailPanelLastOpenedTab) {
                    tabbedPane!!.selectedComponent = tabComponent
                }
            }

        return tabbedPane as JTabbedPane
    }
}