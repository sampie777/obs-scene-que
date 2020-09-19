package plugins.tallyLight

import com.google.gson.Gson
import getCurrentJarDirectory
import jsonBuilder
import objects.notifications.Notifications
import plugins.tallyLight.json.TallyLightJson
import java.io.File
import java.util.logging.Logger

open class TallyLightPropertiesHolder(
    var lightConnectionTimeout: Long = 2000,
    var tallyLightsJson: List<TallyLightJson> = emptyList(),
    var filterSettings: HashMap<String, Any> = hashMapOf()
)

@Suppress("UNCHECKED_CAST")
object TallyLightProperties : TallyLightPropertiesHolder() {
    private val logger = Logger.getLogger(TallyLightProperties.toString())

    // En-/disables the creation of a properties file and writing to a properties file.
    // Leave disabled when running tests.
    var writeToFile: Boolean = false

    private val configFilePath =
        getCurrentJarDirectory(this).absolutePath + File.separatorChar + "tally-light-config.json"

    private fun loadFromJson(json: TallyLightProperties) {
        lightConnectionTimeout = json.lightConnectionTimeout
        tallyLightsJson = json.tallyLightsJson
        filterSettings = json.filterSettings
    }

    fun load() {
        logger.info("Loading TallyLight properties from: $configFilePath")
        val configFile = File(configFilePath)

        if (!configFile.exists()) {
            logger.info("No TallyLight properties file found, using defaults")
            save()
        }

        val jsonText = try {
            configFile.readText(Charsets.UTF_8)
        } catch (t: Throwable) {
            logger.severe("Failed to read TallyLight config file")
            t.printStackTrace()
            Notifications.popup("Failed to read TallyLight config file: ${t.localizedMessage}")
            null
        }

        if (jsonText != null) {
            try {
                val json = Gson().fromJson(jsonText, TallyLightProperties::class.java)
                loadFromJson(json)
            } catch (t: Throwable) {
                logger.severe("Failed to load TallyLight config")
                t.printStackTrace()
                Notifications.popup("Failed to load TallyLight config: ${t.localizedMessage}")
            }
        }

        loadTallyLights()
        loadFilterSettings()
    }

    fun save() {
        logger.info("Saving TallyLight properties")

        saveTallyLights()
        saveFilterSettings()

        if (!writeToFile) {
            return
        }

        logger.info("Saving to TallyLight properties file: $configFilePath")

        val json = try {
            jsonBuilder().toJson(TallyLightProperties)
        } catch (t: Throwable) {
            logger.severe("Failed to save TallyLight config")
            t.printStackTrace()
            Notifications.popup("Failed to save TallyLight config: ${t.localizedMessage}")
            return
        }

        try {
            File(configFilePath).writeText(json, Charsets.UTF_8)
        } catch (t: Throwable) {
            logger.severe("Failed to save TallyLight config to file")
            t.printStackTrace()
            Notifications.popup("Failed to save TallyLight config to file: ${t.localizedMessage}")
        }
    }

    private fun loadTallyLights() {
        val tempList = TallyLightPlugin.tallies.clone() as ArrayList<TallyLight>
        TallyLightPlugin.tallies.clear()
        tallyLightsJson.forEach { tallyLightJson ->
            val tallyLight = TallyLight(
                cameraSourceName = tallyLightJson.cameraSourceName,
                host = tallyLightJson.host,
                isLive = tempList.find { it.cameraSourceName == tallyLightJson.cameraSourceName }?.isLive ?: false
            )
            TallyLightPlugin.tallies.add(tallyLight)
        }
    }

    private fun loadFilterSettings() {
        TallyLightPlugin.filters.forEach {
            try {
                val filterSetting = filterSettings[it.javaClass.name] as Map<String, Any>? ?: return@forEach
                it.loadConfig(filterSetting)
            } catch (t: Throwable) {
                logger.warning("Failed to load settings for Tally Light filter: ${it.javaClass.name}")
                t.printStackTrace()
                Notifications.add("Failed to load settings for Tally Light filter: ${it.javaClass.name}; ${t.localizedMessage}")
            }
        }
    }

    private fun saveTallyLights() {
        tallyLightsJson = TallyLightPlugin.tallies.map {
            TallyLightJson(cameraSourceName = it.cameraSourceName, host = it.host)
        }
    }

    private fun saveFilterSettings() {
        filterSettings = TallyLightPlugin.filters
            .map {
                try {
                    it.javaClass.name to it.saveConfig()
                } catch (t: Throwable) {
                    logger.warning("Failed to save settings for Tally Light filter: ${it.javaClass.name}")
                    t.printStackTrace()
                    Notifications.add("Failed to save settings for Tally Light filter: ${it.javaClass.name}; ${t.localizedMessage}")
                    Pair(it.javaClass.name, null)
                }
            }
            .filter { it.second != null }
            .toMap(HashMap()) as HashMap<String, Any>
    }
}