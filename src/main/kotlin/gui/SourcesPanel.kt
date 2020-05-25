package gui

import config.Config
import objects.notifications.Notifications
import plugins.PluginLoader
import java.awt.BorderLayout
import java.util.logging.Logger
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTabbedPane
import javax.swing.border.EmptyBorder

class SourcesPanel : JPanel() {
    private val logger = Logger.getLogger(SourcesPanel::class.java.name)

    init {
        initGui()
    }

    private fun initGui() {
        layout = BorderLayout()

        val titleLabel = JLabel("Sources")
        titleLabel.border = EmptyBorder(7, 10, 7, 10)
        add(titleLabel, BorderLayout.PAGE_START)

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
        for (plugin in PluginLoader.queItemPlugins.sortedBy { it.name }) {
            val tabComponent: JComponent
            try {
                tabComponent = plugin.sourcePanel()
                tabbedPane.addTab(
                    plugin.tabName,
                    plugin.icon,
                    tabComponent,
                    "${plugin.description} [${plugin.version}]"
                )
            } catch (e: AbstractMethodError) {
                logger.warning("Failed to load panel for plugin: ${plugin.name}")
                e.printStackTrace()
                Notifications.add("Failed to load sources panel for ${plugin.name}", "Plugins")
                continue
            } catch (e: Exception) {
                logger.warning("Failed to load panel for plugin: ${plugin.name}")
                e.printStackTrace()
                Notifications.add("Failed to load sources panel for ${plugin.name}", "Plugins")
                continue
            }

            if (plugin.tabName == Config.sourcePanelLastOpenedTab) {
                tabbedPane.selectedComponent = tabComponent
            }
        }
    }
}