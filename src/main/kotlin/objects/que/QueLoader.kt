package objects.que

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import config.Config
import objects.notifications.Notifications
import plugins.PluginLoader
import plugins.common.QueItem
import java.io.File
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

        // Create backwards compatibility
        if (queFile.extension == "osq") {
            @Suppress("DEPRECATION")
            val queList = queFile.readLines()
                .mapNotNull { loadQueItemForStringLine(it) } as ArrayList<QueItem>
            Que.setList(queList)
        } else {
            val json = queFile.readText()
            fromJson(json)
        }
    }

    fun loadQueItemFromJson(jsonQueItem: JsonQue.QueItem): QueItem? {
        val plugin = PluginLoader.queItemPlugins.find { plugin -> plugin.name == jsonQueItem.pluginName }

        if (plugin == null) {
            Notifications.add("Plugin '${jsonQueItem.pluginName}' not found", "Que")
            return null
        }

        val item: QueItem
        try {
            item = plugin.jsonToQueItem(jsonQueItem)
        } catch (e: Exception) {
            logger.warning("Failed to load que item $jsonQueItem")
            e.printStackTrace()
            Notifications.add(
                "Failed to load que item '${jsonQueItem}' for plugin '${jsonQueItem.pluginName}': $e",
                "Que"
            )
            return null
        }

        item.dataFromJson(jsonQueItem)
        return item
    }

    fun save() {
        if (!writeToFile) {
            logger.info("writeToFile is turned off, so not saving que to file")
            return
        }

        val json = try {
            queToJson()
        } catch (e: Exception) {
            logger.warning("Failed to save Que to file")
            e.printStackTrace()
            Notifications.add("Failed to save Que to file", "Que")
            return
        }

        if (json == lastSavedData) {
            logger.fine("No changes in que, so skipping save")
            return
        }

        val fileName = File(Config.queFile).parentFile.absolutePath + File.separatorChar + Que.name + ".json"
        Config.queFile = fileName
        logger.info("Saving que to file: $fileName")
        File(fileName).writeText(json)

        lastSavedData = json
    }

    fun queToJson(): String {
        val jsonQueItems = Que.getList().mapNotNull {
            try {
                it.toJson()
            } catch (e: Exception) {
                logger.warning("Failed to convert QueItem '${it.name}' to JsonQue.QueItem")
                e.printStackTrace()
                Notifications.add("Failed to save que item ${it.name}", "Que")
                null
            }
        }

        val jsonQue = JsonQue.Que(
            name = Que.name,
            applicationVersion = Que.applicationVersion,
            queItems = jsonQueItems
        )

        try {
            val prettyGson = GsonBuilder().setPrettyPrinting().create()
            return prettyGson.toJson(jsonQue)
        } catch (e: Exception) {
            logger.warning("Failed to convert JsonQue.Que to string: $jsonQue")
            e.printStackTrace()
            throw e
        }
    }

    fun fromJson(json: String) {
        val jsonQue = jsonQueFromJson(json) ?: return

        Que.name = jsonQue.name
        Que.applicationVersion = jsonQue.applicationVersion
        Que.clear()
        jsonQue.queItems.forEach {
            loadQueItemFromJson(it)?.let { it1 -> Que.add(it1) }
        }
    }

    private fun jsonQueFromJson(json: String): JsonQue.Que? {
        return try {
            Gson().fromJson(json, JsonQue.Que::class.java)
        } catch (e: Exception) {
            logger.warning("Failed to load Que from json: $json")
            e.printStackTrace()
            Notifications.add("Failed to load Que from json", "Que")
            null
        }
    }

    fun jsonQueItemFromJson(json: String): JsonQue.QueItem? {
        return try {
            Gson().fromJson(json, JsonQue.QueItem::class.java)
        } catch (e: Exception) {
            logger.warning("Failed to load Que Item from json: $json")
            e.printStackTrace()
            Notifications.add("Failed to load Que Item from json", "Que")
            null
        }
    }

    @Deprecated(
        "Use JSON converter instead of this plane text converter",
        ReplaceWith(
            "QueLoader.loadQueItemFromJson(jsonQueItem: JsonQue.QueItem)",
            "objects.que.QueLoader\nobjects.que.JsonQue"
        )
    )
    @Suppress("DEPRECATION")
    fun loadQueItemForStringLine(line: String): QueItem? {
        if (line.isEmpty()) {
            return null
        }

        val stringData = line.split("|", limit = 3)
        if (stringData.size != 3) {
            logger.info("Invalid que item string line: $line")
            return null
        }

        val pluginName = stringData[0]
        val executeAfterPrevious = stringData[1] == "true"
        val data = stringData[2]


        val plugin = PluginLoader.queItemPlugins.find { plugin -> plugin.name == pluginName }

        if (plugin == null) {
            Notifications.add("Plugin '$pluginName' not found", "Que")
            return null
        }

        try {
            val item = plugin.configStringToQueItem(data)
            item.executeAfterPrevious = executeAfterPrevious
            return item
        } catch (e: Exception) {
            logger.warning("Failed to load que item $line")
            e.printStackTrace()
            Notifications.add("Failed to load que item '$data' for '$pluginName': $e", "Que")
            return null
        }
    }

    @Deprecated(
        "Use JSON converter instead of this plane text converter",
        ReplaceWith("QueLoader.queToJson()", "objects.que.QueLoader")
    )
    @Suppress("DEPRECATION")
    fun queToString(): String {
        return Que.getList().joinToString("\n") {
            queItemToConfigString(it)
        }
    }

    @Deprecated(
        "Use JSON converter instead of this plane text converter",
        ReplaceWith("queItem.toJson()", "")
    )
    @Suppress("DEPRECATION")
    fun queItemToConfigString(queItem: QueItem) =
        queItem.plugin.name + "|" + queItem.executeAfterPrevious + "|" + queItem.toConfigString()
}