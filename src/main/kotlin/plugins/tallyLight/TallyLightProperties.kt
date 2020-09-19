package plugins.tallyLight

import com.google.gson.Gson
import getCurrentJarDirectory
import jsonBuilder
import objects.notifications.Notifications
import java.io.File
import java.util.logging.Logger

open class TallyLightPropertiesHolder(
    var lightConnectionTimeout: Long = 2000,
    var enableRehobothFilter: Boolean = false,
    var tallyLightsJson: List<TallyLightJson> = emptyList()
) {
    data class TallyLightJson(
        var cameraSourceName: String,
        var host: String = ""
    )
}

object TallyLightProperties : TallyLightPropertiesHolder() {
    private val logger = Logger.getLogger(TallyLightProperties.toString())

    // En-/disables the creation of a properties file and writing to a properties file.
    // Leave disabled when running tests.
    var writeToFile: Boolean = false

    private val configFilePath =
        getCurrentJarDirectory(this).absolutePath + File.separatorChar + "tally-light-config.json"

    private fun loadFromJson(json: TallyLightProperties) {
        lightConnectionTimeout = json.lightConnectionTimeout
        enableRehobothFilter = json.enableRehobothFilter
        tallyLightsJson = json.tallyLightsJson
    }

    @Suppress("UNCHECKED_CAST")
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
    }

    fun save() {
        logger.info("Saving TallyLight properties")

        saveTallyLights()

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

    private fun saveTallyLights() {
        tallyLightsJson = TallyLightPlugin.tallies.map {
            TallyLightJson(cameraSourceName = it.cameraSourceName, host = it.host)
        }
    }
}