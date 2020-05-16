package plugins.easyworship

import getCurrentJarDirectory
import java.awt.event.KeyEvent
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*
import java.util.logging.Logger

object EasyWorshipProperties {
    private val logger = Logger.getLogger(EasyWorshipProperties.toString())

    // En-/disables the creation of a properties file and writing to a properties file.
    // Leave disabled when running tests.
    var writeToFile: Boolean = false

    private val propertiesFilePath = getCurrentJarDirectory(this).absolutePath + File.separatorChar + "osq-easyworship.properties"
    private val properties = Properties()

    var previousVerseKey: Int = -1
    var nextVerseKey: Int = -1
    var previousSongKey: Int = -1
    var nextSongKey: Int = -1
    var logoScreenKey: Int = -1
    var blackScreenKey: Int = -1
    var clearScreenKey: Int = -1

    fun load() {
        logger.info("Loading easyworship properties")

        if (File(propertiesFilePath).exists()) {
            FileInputStream(propertiesFilePath).use { properties.load(it) }
        } else {
            logger.info("No easyworship properties file found, using defaults")
        }

        previousVerseKey = properties.getProperty("previousVerseKey", KeyEvent.VK_UP.toString()).toInt()
        nextVerseKey = properties.getProperty("nextVerseKey", KeyEvent.VK_DOWN.toString()).toInt()
        previousSongKey = properties.getProperty("previousSongKey", KeyEvent.VK_PAGE_UP.toString()).toInt()
        nextSongKey = properties.getProperty("nextSongKey", KeyEvent.VK_ENTER.toString()).toInt()
        logoScreenKey = properties.getProperty("logoScreenKey", KeyEvent.VK_L.toString()).toInt()
        blackScreenKey = properties.getProperty("blackScreenKey", KeyEvent.VK_B.toString()).toInt()
        clearScreenKey = properties.getProperty("clearScreenKey", KeyEvent.VK_C.toString()).toInt()

        if (!File(propertiesFilePath).exists()) {
            save()
        }
    }

    fun save() {
        logger.info("Saving easyworship properties")
        properties.setProperty("previousVerseKey", previousVerseKey.toString())
        properties.setProperty("nextVerseKey", nextVerseKey.toString())
        properties.setProperty("previousSongKey", previousSongKey.toString())
        properties.setProperty("nextSongKey", nextSongKey.toString())
        properties.setProperty("logoScreenKey", logoScreenKey.toString())
        properties.setProperty("blackScreenKey", blackScreenKey.toString())
        properties.setProperty("clearScreenKey", clearScreenKey.toString())

        if (!writeToFile) {
            return
        }

        logger.info("Creating easyworship properties file")

        FileOutputStream(propertiesFilePath).use { fileOutputStream ->
            properties.store(
                fileOutputStream,
                "User properties for EasyWorship plugin"
            )
        }
    }
}