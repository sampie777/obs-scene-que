package gui.menu

import objects.notifications.Notifications
import plugins.PluginLoader
import themes.Theme
import java.util.logging.Logger
import javax.swing.BorderFactory
import javax.swing.JMenu

class PluginMenu : JMenu("Plugins") {
    private val logger = Logger.getLogger(PluginMenu::class.java.name)

    init {
        initGui()
    }

    private fun initGui() {
        popupMenu.border = BorderFactory.createLineBorder(Theme.get.BORDER_COLOR)

        PluginLoader.allPlugins.forEach {
            try {
                val menu = JMenu(it.name)
                menu.popupMenu.border = BorderFactory.createLineBorder(Theme.get.BORDER_COLOR)

                if (it.createMenu(menu)) {
                    add(menu)
                }
            } catch (e: AbstractMethodError) {
                logger.warning("Failed to create menu(items) for plugin: ${it.name}")
                e.printStackTrace()
                Notifications.add("Failed to create menu(items) for plugin: ${it.name}", "Plugins")
            } catch (e: Exception) {
                logger.warning("Failed to create menu(items) for plugin: ${it.name}")
                e.printStackTrace()
                Notifications.add("Failed to create menu(items) for plugin: ${it.name}", "Plugins")
            }
        }

        if (menuComponentCount == 0) {
            isVisible = false
        }
    }
}