import com.google.gson.Gson
import com.google.gson.GsonBuilder
import config.Config
import gui.mainFrame.MainFrame
import objects.OBSClient
import objects.notifications.Notifications
import objects.que.Que
import org.jnativehook.keyboard.NativeKeyEvent
import plugins.PluginLoader
import java.awt.Color
import java.awt.Desktop
import java.io.File
import java.io.UnsupportedEncodingException
import java.net.URI
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.Logger
import kotlin.system.exitProcess

private val logger: Logger = Logger.getLogger("utils")

fun getTimeAsClock(seconds: Long): String {
    var positiveValue = seconds

    var signString = ""
    if (seconds < 0) {
        positiveValue *= -1
        signString = "-"
    }

    val timerHours = positiveValue / 3600
    val timerMinutes = (positiveValue - timerHours * 3600) / 60
    val timerSeconds = positiveValue - timerHours * 3600 - timerMinutes * 60
    return String.format("%s%d:%02d:%02d", signString, timerHours, timerMinutes, timerSeconds)
}

@Throws(UnsupportedEncodingException::class)
fun getCurrentJarDirectory(caller: Any): File {
    val url = caller::class.java.protectionDomain.codeSource.location
    val jarPath = URLDecoder.decode(url.file, "UTF-8")
    return File(jarPath).parentFile
}

fun isAddressLocalhost(address: String): Boolean {
    return address.contains("localhost") || address.contains("127.0.0.1")
}

fun exitApplication() {
    logger.info("Shutting down application")

    MainFrame.getInstance()?.saveWindowPosition()

    try {
        logger.info("Stopping OBS client...")
        OBSClient.stop()
    } catch (t: Throwable) {
        logger.warning("Failed to correctly stop OBS client")
        t.printStackTrace()
    }

    try {
        logger.info("Closing windows...")
        GUI.windowClosing(MainFrame.getInstance())
    } catch (t: Throwable) {
        logger.warning("Failed to correctly close windows")
        t.printStackTrace()
    }

    try {
        logger.info("Disabling plugins...")
        PluginLoader.disableAll()
    } catch (t: Throwable) {
        logger.warning("Failed to disable plugins")
        t.printStackTrace()
    }

    try {
        logger.info("Saving configuration...")
        Que.save()
        Config.save()
    } catch (t: Throwable) {
        logger.warning("Failed to save configuration")
        t.printStackTrace()
    }

    // This seems to prevent application shutdown
//    GlobalKeyboardHook.unregister()

    logger.info("Shutdown finished")
    exitProcess(0)
}

fun brightness(color: Color): Double {
    return 0.2126 * color.red + 0.7152 * color.green + 0.0722 * color.blue
}

fun decodeURI(uri: String): String {
    return URLDecoder.decode(uri, StandardCharsets.UTF_8.name())
}

fun getReadableFileSize(file: File): String {
    return when {
        file.length() > 1024 * 1024 -> {
            val fileSize = file.length().toDouble() / (1024 * 1024)
            String.format("%.2f MB", fileSize)
        }
        file.length() > 1024 -> {
            val fileSize = file.length().toDouble() / 1024
            String.format("%.2f kB", fileSize)
        }
        else -> {
            String.format("%d bytes", file.length())
        }
    }
}

fun getFileNameWithoutExtension(file: File): String {
    return file.name.substring(0, file.name.lastIndexOf('.'))
}

fun getFileExtension(file: File): String {
    return file.name.substring(file.name.lastIndexOf('.') + 1)
}

fun Date.format(format: String): String? = SimpleDateFormat(format).format(this)

internal fun jsonBuilder(prettyPrint: Boolean = true): Gson {
    val builder = GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
        .serializeNulls()

    if (prettyPrint) {
        builder.setPrettyPrinting()
    }

    return builder.create()
}


fun keyEventToString(e: NativeKeyEvent?): String {
    if (e == null) {
        return ""
    }

    return listOf(
        NativeKeyEvent.getModifiersText(e.modifiers),
        NativeKeyEvent.getKeyText(e.keyCode)
    )
        .filter { !it.isBlank() }
        .joinToString("+")
}

fun openWebURL(url: String, subject: String = "Webbrowser"): Boolean {
    if (!Desktop.isDesktopSupported()) {
        logger.warning("Cannot open link '$url': not supported by host")
        return false
    }
    try {
        Desktop.getDesktop().browse(URI(url))
        return true
    } catch (t: Throwable) {
        logger.severe("Error during opening link '$url'")
        t.printStackTrace()
        Notifications.popup("Failed to open link: $url", subject)
    }
    return false
}