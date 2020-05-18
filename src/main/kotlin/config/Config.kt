package config

import getCurrentJarDirectory
import objects.notifications.Notifications
import java.awt.Dimension
import java.awt.Point
import java.io.File
import java.util.logging.Logger

object Config {
    private val logger = Logger.getLogger(Config.toString())

    var obsAddress: String = "ws://localhost:4444"
    var obsPassword: String = ""
    var obsReconnectionTimeout: Long = 3000

    var theme: String = "LightTheme"
    var windowRestoreLastPosition: Boolean = true
    var mainWindowLocation: Point = Point(0, 0)
    var mainWindowSize: Dimension = Dimension(1000, 600)
    var mainWindowsIsMaximized: Boolean = false
    var controlWindowLocation: Point = Point(-1, -1)
    var controlWindowSize: Dimension = Dimension(500, 250)
    var controlWindowsIsMaximized: Boolean = false
    var mainPanelDividerLocation: Int = 370

    var sourcePanelLastOpenedTab: String = "OBS"

    var queFile: String = getCurrentJarDirectory(this).absolutePath + File.separatorChar + "default.osq"
    var pluginDirectory: String = getCurrentJarDirectory(this).absolutePath + File.separatorChar + "plugins"

    var quickAccessButtonCount: Int = 6
    var quickAccessButtonQueItems: ArrayList<String> = ArrayList()

    fun load() {
        try {
            PropertyLoader.load()
            PropertyLoader.loadConfig(this::class.java)
        } catch (e: Exception) {
            logger.severe("Failed to load Config")
            e.printStackTrace()
            Notifications.add("Failed to load configuration from file", "Configuration")
        }
    }

    fun save() {
        try {
            if (PropertyLoader.saveConfig(this::class.java)) {
                PropertyLoader.save()
            }
        } catch (e: Exception) {
            logger.severe("Failed to save Config")
            e.printStackTrace()
            Notifications.add("Failed to save configuration to file", "Configuration")
        }
    }

    fun get(key: String): Any? {
        try {
            return javaClass.getDeclaredField(key).get(this)
        } catch (e: Exception) {
            logger.severe("Could not get config key $key")
            e.printStackTrace()
            Notifications.add("Could not get configuration setting: $key", "Configuration")
        }
        return null
    }

    fun set(key: String, value: Any?) {
        try {
            javaClass.getDeclaredField(key).set(this, value)
        } catch (e: Exception) {
            logger.severe("Could not set config key $key")
            e.printStackTrace()
            Notifications.add("Could not set configuration setting: $key", "Configuration")
        }
    }

    fun enableWriteToFile(value: Boolean) {
        PropertyLoader.writeToFile = value
    }
}