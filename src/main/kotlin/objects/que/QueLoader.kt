package objects.que

import com.google.gson.Gson
import config.Config
import jsonBuilder
import objects.notifications.Notifications
import plugins.PluginLoader
import java.io.File
import java.util.logging.Logger


internal object QueLoader {
    private val logger = Logger.getLogger(QueLoader.toString())

    // En-/disables the creation of a properties file and writing to a properties file.
    // Leave disabled when running tests.
    var writeToFile: Boolean = false

    private var lastSavedData: String = ""

    fun load(): Boolean {
        return load(File(Config.queFile))
    }

    fun load(queFile: File): Boolean {
        logger.info("Loading queue from file: ${queFile.absolutePath}")

        lastSavedData = ""

        if (!queFile.exists()) {
            logger.info("Queue file not found")
            Que.clear()

            if (queFile.nameWithoutExtension != Que.defaultName) {
                Notifications.popup(
                    "Queue file not found: \n${queFile.absolutePath}",
                    "Queue"
                )
            }
            return false
        }

        // Create backwards compatibility
        if (queFile.extension == "osq") {
            @Suppress("DEPRECATION")
            val queList = try {
                queFile.readLines()
                    .mapNotNull { loadQueItemForStringLine(it) } as ArrayList<QueItem>
            } catch (e: Exception) {
                logger.severe("Failed to json queue from file")
                e.printStackTrace()
                Notifications.popup("Failed to read file: ${e.localizedMessage}", "Queue")
                return false
            }

            Que.setList(queList)
        } else {
            val json = try {
                queFile.readText()
            } catch (e: Exception) {
                logger.severe("Failed to json queue from file")
                e.printStackTrace()
                Notifications.popup("Failed to read file: ${e.localizedMessage}", "Queue")
                return false
            }

            if (!fromJson(json)) {
                logger.info("Could not load Queue from json")
                return false
            }
        }

        val fileName = queFile.nameWithoutExtension
        if (fileName != Que.name) {
            logger.info("Renaming que to file name: $fileName")
            Que.name = fileName
        }

        Config.queFile = queFile.absolutePath
        return true
    }

    fun loadQueItemFromJson(jsonQueueItem: JsonQueue.QueueItem): QueItem? {
        val plugin =
            if (jsonQueueItem.pluginName == "TextPlugin") {     // Backwards compatible from version 2.6.0
                logger.info("Loading UtilityPlugin for TextPlugin as backwards compatibility for Queue Item: $jsonQueueItem")
                PluginLoader.queItemPlugins.find { plugin -> plugin.name == "UtilityPlugin" }
            } else {
                PluginLoader.queItemPlugins.find { plugin -> plugin.name == jsonQueueItem.pluginName }
            }

        if (plugin == null) {
            Notifications.add("Plugin '${jsonQueueItem.pluginName}' not found", "Queue")
            return null
        }

        val item: QueItem
        try {
            item = plugin.jsonToQueItem(jsonQueueItem)
            item.dataFromJson(jsonQueueItem)
        } catch (e: Exception) {
            logger.warning("Failed to load queue item $jsonQueueItem")
            e.printStackTrace()
            Notifications.add(
                "Failed to load queue item '${jsonQueueItem}' for plugin '${jsonQueueItem.pluginName}': $e",
                "Queue"
            )
            return null
        }

        return item
    }

    fun save(): Boolean {
        return save(File(Config.queFile))
    }

    fun save(queFile: File): Boolean {
        Que.name = queFile.nameWithoutExtension

        val json = try {
            queToJsonString()
        } catch (e: Exception) {
            logger.warning("Failed to convert Queue to json")
            e.printStackTrace()
            Notifications.popup("Failed to save Queue to file: ${e.localizedMessage}", "Queue")
            return false
        }

        if (json == lastSavedData) {
            logger.fine("No changes in queue, so skipping save")
            return true
        }

        if (!writeToFile) {
            logger.info("writeToFile is turned off, so not saving queue to file")
        } else {
            logger.info("Saving queue to file: $queFile")
            try {
                queFile.writeText(json)
            } catch (e: Exception) {
                logger.severe("Failed to save json to file")
                e.printStackTrace()
                Notifications.popup("Failed to save Queue to file: ${e.localizedMessage}", "Queue")
                return false
            }
        }

        lastSavedData = json
        Config.queFile = queFile.absolutePath
        return true
    }

    fun queToJsonString(): String {
        val jsonQueue = queueToJsonObject()

        try {
            return jsonBuilder().toJson(jsonQueue)
        } catch (e: Exception) {
            logger.warning("Failed to convert JsonQueue.Queue to string: $jsonQueue")
            e.printStackTrace()
            throw e
        }
    }

    fun queueToJsonObject(): JsonQueue.Queue {
        val jsonQueItems = Que.getList().mapNotNull {
            try {
                it.toJson()
            } catch (e: Exception) {
                logger.warning("Failed to convert QueItem '${it.name}' to JsonQueue.QueItem")
                e.printStackTrace()
                Notifications.add("Failed to save queue item ${it.name}: ${e.localizedMessage}", "Queue")
                null
            }
        }

        return JsonQueue.Queue(
            name = Que.name,
            applicationVersion = Que.applicationVersion,
            queueItems = jsonQueItems
        )
    }

    fun fromJson(json: String): Boolean {
        if (json.isBlank()) {
            logger.warning("Cannot load empty json string as Queue")
            Notifications.popup("Failed to load Queue from file: file is empty", "Queue")
            return false
        }

        try {
            val jsonQue = jsonQueFromJson(json) ?: return false

            Que.name = jsonQue.name
            Que.applicationVersion = jsonQue.applicationVersion
            Que.clear()

            jsonQue.queueItems.forEach {
                loadQueItemFromJson(it)?.let { it1 -> Que.add(it1) }
            }
            return true
        } catch (e: Exception) {
            logger.warning("Failed to load Queue from JSON: $json")
            e.printStackTrace()
            Notifications.popup("Failed to load Queue from file: ${e.localizedMessage}", "Queue")
        }
        return false
    }

    private fun jsonQueFromJson(json: String): JsonQueue.Queue? {
        return try {
            val compatibleJson = json.let {
                val apiVersionPredicate = """"apiVersion":[\s\n\r]*(\d+),""".toRegex()
                val result = apiVersionPredicate.find(it)

                // Make JSON text compatible with new JsonQueue.Queue API
                if (result == null) {
                    val queItemsPredicate = """"queItems":[\s\n\r]*\[""".toRegex()
                    return@let queItemsPredicate.replace(json, "\"queueItems\": [")
                }

                json
            }

            Gson().fromJson(compatibleJson, JsonQueue.Queue::class.java)
        } catch (e: Exception) {
            logger.warning("Failed to load Queue from json: $json")
            e.printStackTrace()
            Notifications.add("Failed to load Queue from json: ${e.localizedMessage}", "Queue")
            null
        }
    }

    fun jsonQueItemFromJson(json: String): JsonQueue.QueueItem? {
        return try {
            Gson().fromJson(json, JsonQueue.QueueItem::class.java)
        } catch (e: Exception) {
            logger.warning("Failed to load Queue Item from json: $json")
            e.printStackTrace()
            Notifications.add("Failed to load Queue Item from json: ${e.localizedMessage}", "Queue")
            null
        }
    }

    @Deprecated(
        "Use JSON converter instead of this plane text converter",
        ReplaceWith(
            "QueLoader.loadQueItemFromJson(jsonQueItem: JsonQueue.QueItem)",
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
            logger.info("Invalid queue item string line: $line")
            return null
        }

        val pluginName = stringData[0]
        val executeAfterPrevious = stringData[1] == "true"
        val data = stringData[2]


        val plugin = PluginLoader.queItemPlugins.find { plugin -> plugin.name == pluginName }

        if (plugin == null) {
            Notifications.add("Plugin '$pluginName' not found", "Queue")
            return null
        }

        try {
            val item = plugin.configStringToQueItem(data)
            item.executeAfterPrevious = executeAfterPrevious
            return item
        } catch (e: Exception) {
            logger.warning("Failed to load queue item $line")
            e.printStackTrace()
            Notifications.add("Failed to load queue item '$data' for '$pluginName': ${e.localizedMessage}", "Queue")
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