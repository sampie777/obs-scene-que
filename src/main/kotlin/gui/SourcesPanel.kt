package gui

import config.Config
import plugins.PluginLoader
import java.awt.BorderLayout
import java.util.logging.Logger
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JTabbedPane

class SourcesPanel : JPanel() {
    private val logger = Logger.getLogger(SourcesPanel::class.java.name)

    init {
        initGui()
    }

    private fun initGui() {
        layout = BorderLayout()

        val tabbedPane = JTabbedPane()
        tabbedPane.tabPlacement = JTabbedPane.TOP
        tabbedPane.border = null
        add(tabbedPane, BorderLayout.CENTER)

        addPluginsToTabbedPane(tabbedPane)

        // Add change listener after plugins are added, otherwise the listener will be fired while adding the plugins
        tabbedPane.addChangeListener {
            logger.fine("Selecting tab: " + tabbedPane.getTitleAt(tabbedPane.selectedIndex))
            Config.sourcePanelLastOpenedTab = tabbedPane.getTitleAt(tabbedPane.selectedIndex)
        }
    }

    private fun addPluginsToTabbedPane(tabbedPane: JTabbedPane) {
        for (plugin in PluginLoader.plugins.sortedBy { it.name }) {
            val tabComponent: JComponent
            try {
                tabComponent = plugin.sourcePanel()
                tabbedPane.addTab(plugin.tabName, plugin.icon, tabComponent, plugin.description)
            } catch (e: Exception) {
                logger.warning("Failed to load panel for plugin: ${plugin.name}")
                e.printStackTrace()
                continue
            }

            if (plugin.tabName == Config.sourcePanelLastOpenedTab) {
                tabbedPane.selectedComponent = tabComponent
            }
        }
    }
}