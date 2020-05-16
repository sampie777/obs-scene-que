import config.Config
import gui.MainFrame
import objects.ApplicationInfo
import objects.OBSClient
import objects.que.Que
import plugins.PluginLoader
import themes.Theme
import java.awt.EventQueue
import java.util.logging.Logger

fun main(args: Array<String>) {
    val logger = Logger.getLogger("Application")
    logger.info("Starting application ${ApplicationInfo.artifactId}:${ApplicationInfo.version}")
    logger.info("Executing JAR directory: " + getCurrentJarDirectory(Config).absolutePath)

    Config.enableWriteToFile(true)
    Config.load()
    Config.save()

    Theme.init()

    PluginLoader.loadAll()
    PluginLoader.enableAll()

    Que.enableWriteToFile(true)
    Que.load()

    EventQueue.invokeLater {
        MainFrame.createAndShow()
    }

    OBSClient.start()
}