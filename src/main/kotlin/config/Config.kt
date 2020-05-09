package config

import objects.Que
import objects.TScene
import objects.notifications.Notifications
import java.util.logging.Logger

object Config {
    private val logger = Logger.getLogger(Config.toString())

    var obsAddress: String = "ws://localhost:4444"
    var obsPassword: String = ""
    var obsReconnectionTimeout: Long = 3000

    var queSceneNames: ArrayList<String> = ArrayList()

    var windowRestoreLastPosition: Boolean = false
    var windowLocationX: Int = 0
    var windowLocationY: Int = 0
    var windowLocationWidth: Int = 1000
    var windowLocationHeight: Int = 600

    fun load() {
        try {
            PropertyLoader.load()
            PropertyLoader.loadConfig(this::class.java)

            Que.clear()
            queSceneNames.forEach {
                val scene = TScene()
                scene.name = it
                Que.add(scene)
            }
        } catch (e: Exception) {
            logger.severe("Failed to load Config")
            e.printStackTrace()
            Notifications.add("Failed to load configuration from file", "Configuration")
        }
    }

    fun save() {
        try {
            queSceneNames.clear()
            Que.getList().forEach {
                queSceneNames.add(it.name)
            }

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