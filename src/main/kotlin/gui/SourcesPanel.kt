package gui

import plugins.PluginLoader
import java.util.logging.Logger
import javax.swing.JPanel
import javax.swing.JTabbedPane

class SourcesPanel : JPanel() {
    private val logger = Logger.getLogger(SourcesPanel::class.java.name)


    init {
        initGui()
    }

    private fun initGui() {
        val tabbedPane = JTabbedPane()
        tabbedPane.tabPlacement = JTabbedPane.TOP
        tabbedPane.border = null
        tabbedPane.addChangeListener {
            logger.fine("Selecting tab: " + tabbedPane.getTitleAt(tabbedPane.selectedIndex))
        }
        add(tabbedPane)

        for (plugin in PluginLoader.plugins) {
            try {
                tabbedPane.addTab(plugin.tabName, plugin.icon, plugin.sourcePanel(), plugin.description)
            } catch (e: Exception) {
                logger.warning("Failed to load panel for plugin: ${plugin.name}")
                e.printStackTrace()
            }
        }
    }
}