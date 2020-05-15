package objects.que

import config.Config
import objects.notifications.Notifications
import plugins.PluginLoader
import plugins.common.QueItem
import java.io.File
import java.util.*
import java.util.logging.Logger

internal object QueLoader {
    private val logger = Logger.getLogger(QueLoader.toString())

    // En-/disables the creation of a properties file and writing to a properties file.
    // Leave disabled when running tests.
    var writeToFile: Boolean = false

    private var lastSavedData: String = ""

    fun load() {
        logger.info("Loading que from file")
        val queFile = File(Config.queFile)

        if (!queFile.exists()) {
            logger.info("Que file not found")
            Que.clear()
            return
        }

        val queList = queFile.readLines()
            .map {
                val pluginName = it.substringAfter("[").substringBefore("|")
                val data = it.substringAfter("|").substringBeforeLast("]")

                val plugin = PluginLoader.plugins.find { plugin -> plugin.name == pluginName }

                if (plugin == null) {
                    Notifications.add("Plugin '$pluginName' not found", "Que")
                    return@map null
                }

                try {
                    plugin.configStringToQueItem(data)
                } catch (e: Exception) {
                    logger.warning("Failed to load que item $it")
                    e.printStackTrace()
                    Notifications.add("Failed to load que item '$data' for '$pluginName': $e", "Que")
                    return@map null
                }
            }
            .filterNotNull() as ArrayList<QueItem>

        Que.setList(queList)
    }

    fun save() {
        if (!writeToFile) {
            logger.info("writeToFile is turned off, so not saving que to file")
            return
        }

        val textData = Que.getList().joinToString("\n") {
            "[" + it.plugin.name + "|" + it.toConfigString() + "]"
        }

        if (textData == lastSavedData) {
            logger.fine("No changes in que, so skipping save")
            return
        }

        logger.info("Saving que to file")
        File(Config.queFile).writeText(textData)

        lastSavedData = textData
    }
}