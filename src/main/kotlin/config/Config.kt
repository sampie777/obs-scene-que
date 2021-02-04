package config

import getCurrentJarDirectory
import objects.json.NativeKeyEventJson
import objects.notifications.Notifications
import objects.que.Que
import java.awt.Dimension
import java.awt.Point
import java.io.File
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.util.logging.Logger

object Config {
    private val logger = Logger.getLogger(Config.toString())

    // OBS Connection
    var obsAddress: String = "ws://localhost:4444"
    var obsPassword: String = ""
    var obsReconnectionTimeout: Long = 3000

    // Logging
    var enableApplicationLoggingToFile: Boolean = true
    var maxLogFileSize: Int = 1024 * 1024    // 1 MB

    // Window Layout
    var theme: String = "LightTheme"
    var windowRestoreLastPosition: Boolean = true
    var mainWindowLocation: Point = Point(0, 0)
    var mainWindowSize: Dimension = Dimension(1300, 760)
    var mainWindowsIsMaximized: Boolean = false
    var mainWindowsIsFullscreen: Boolean = false
    var controlWindowLocation: Point = Point(-1, -1)
    var controlWindowSize: Dimension = Dimension(500, 250)
    var controlWindowsIsMaximized: Boolean = false
    var mainPanelDividerLeftLocation: Int = 370
    var mainPanelDividerRightLocation: Int = 400
    var rightMainPanelDividerLocation: Int = 400
    var mainWindowAlwaysOnTop: Boolean = false

    var sourcePanelLastOpenedTab: String = "OBS"
    var detailPanelLastOpenedTab: String = ""

    var pluginDirectory: String = getCurrentJarDirectory(this).absolutePath + File.separatorChar + "plugins"
    var queFile: String = getCurrentJarDirectory(this).absolutePath + File.separatorChar + Que.name + ".json"
    var recentQueueFiles: HashSet<String> = HashSet()

    var quickAccessButtonCount: Int = 24
    var quickAccessButtonQueItems: ArrayList<String> = ArrayList()
    var quickAccessButtonDisplayIcon: Boolean = true

    // Queue control
    var globalKeyEventNextQueueItem: NativeKeyEventJson? = null
    var globalKeyEventPreviousQueueItem: NativeKeyEventJson? = null
    var activateNextSubQueueItemsOnMouseActivationQueueItem: Boolean = true
    var activateNextSubQueueItemsOnMouseActivationSubQueueItem: Boolean = false

    var httpApiServerEnabled: Boolean = false
    var httpApiServerPort: Int = 8080

    // Other
    var updatesCheckForUpdates: Boolean = true

    fun load() {
        try {
            PropertyLoader.load()
            PropertyLoader.loadConfig(this::class.java)
        } catch (e: Exception) {
            logger.severe("Failed to load Config")
            e.printStackTrace()
            Notifications.add("Failed to load configuration from file: ${e.localizedMessage}", "Configuration")
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
            Notifications.add("Failed to save configuration to file: ${e.localizedMessage}", "Configuration")
        }
    }

    fun get(key: String): Any? {
        try {
            return javaClass.getDeclaredField(key).get(this)
        } catch (e: Exception) {
            logger.severe("Could not get config key $key")
            e.printStackTrace()
            Notifications.add("Could not get configuration setting: $key (${e.localizedMessage})", "Configuration")
        }
        return null
    }

    fun set(key: String, value: Any?) {
        try {
            javaClass.getDeclaredField(key).set(this, value)
        } catch (e: Exception) {
            logger.severe("Could not set config key $key")
            e.printStackTrace()
            Notifications.add("Could not set configuration setting: $key (${e.localizedMessage})", "Configuration")
        }
    }

    fun enableWriteToFile(value: Boolean) {
        PropertyLoader.writeToFile = value
    }

    fun fields(): List<Field> {
        val fields = javaClass.declaredFields.filter {
            it.name != "INSTANCE" && it.name != "logger"
                    && Modifier.isStatic(it.modifiers)
        }
        fields.forEach { it.isAccessible = true }
        return fields
    }
}