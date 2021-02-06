package updater

import com.google.gson.Gson
import com.google.gson.JsonParseException
import config.Config
import gui.updater.UpdatePopup
import objects.ApplicationInfo
import objects.notifications.Notifications
import java.awt.EventQueue
import java.net.MalformedURLException
import java.util.logging.Logger
import java.util.prefs.Preferences


class UpdateChecker(private val urlProvider: wURL = wURL()) {
    private val logger = Logger.getLogger(UpdateChecker::class.java.name)
    private val persistentSettings = Preferences.userRoot().node(UpdateChecker::class.java.name)
    private val persistentSettingsVersionReference = "latestKnownVersion"

    private var latestVersion: String? = null

    fun checkForUpdates() {
        if (!Config.updatesCheckForUpdates) {
            return
        }

        Thread {
            try {
                checkForUpdatesThread()
            } catch (t: Throwable) {
                logger.severe("Failed to check for updates")
                t.printStackTrace()
                Notifications.add(
                    "Failed to check for updates. More detailed: ${t.localizedMessage}",
                    "Updater"
                )
            }
        }.start()
    }

    private fun checkForUpdatesThread() {
        logger.info("Checking for updates...")
        if (!isNewUpdateAvailable() || latestVersion == null) {
            logger.info("No new updates available")
            return
        }

        logger.info("New update found: $latestVersion")
        showNewUpdatePopup()
    }

    private fun showNewUpdatePopup() {
        EventQueue.invokeLater {
            UpdatePopup.createAndShow(latestVersion!!)
        }
    }

    fun isNewUpdateAvailable(): Boolean {
        latestVersion = getLatestVersion()?.trimStart('v')

        if (latestVersion == null) {
            logger.info("No latest version found: $latestVersion")
            return false
        }

        logger.info("Latest version from remote: $latestVersion")

        if (latestVersion == ApplicationInfo.version) {
            logger.info("Application up to date")
            return false
        }

        if (latestVersion == getLatestKnownVersion()) {
            logger.info("Latest version hasn't changed")
            return false
        }
        updateLatestKnownVersion(latestVersion!!)

        return true
    }

    fun getLatestVersion(): String? {
        val jsonResponse = getLatestVersionResponse() ?: return null

        return try {
            Gson().fromJson(jsonResponse, LatestVersionResponseJson::class.java).tag_name
        } catch (e: JsonParseException) {
            logger.severe("Failed to parse version JSON response: '${jsonResponse}'")
            e.printStackTrace()
            Notifications.add(
                "Failed to check for updates: invalid JSON response. " +
                        "Please inform the developer of this application. More detailed: ${e.localizedMessage}.",
                "Updater"
            )
            null
        } catch (t: Throwable) {
            logger.severe("Failed to retrieve latest application version: invalid response")
            t.printStackTrace()
            Notifications.add(
                "Failed to check for updates: invalid response. " +
                        "Please inform the developer of this application. More detailed: ${t.localizedMessage}.",
                "Updater"
            )
            null
        }
    }

    fun getLatestVersionResponse(): String? {
        return try {
            urlProvider.readText(ApplicationInfo.latestVersionsUrl)
        } catch (e: MalformedURLException) {
            logger.severe("Failed to retrieve latest application version: invalid URL: '${ApplicationInfo.latestVersionsUrl}'")
            e.printStackTrace()
            Notifications.add(
                "Failed to check for updates: invalid URL. " +
                        "Please inform the developer of this application. More detailed: ${e.localizedMessage}.",
                "Updater"
            )
            null
        } catch (t: Throwable) {
            logger.severe("Failed to retrieve latest application version")
            t.printStackTrace()
            null
        }
    }

    fun updateLatestKnownVersion(version: String) = persistentSettings.put(persistentSettingsVersionReference, version)

    fun getLatestKnownVersion(): String = persistentSettings.get(persistentSettingsVersionReference, "")

    fun clearUpdateHistory() {
        logger.info("Clearing update history")
        persistentSettings.put(persistentSettingsVersionReference, "")
    }
}