import config.Config
import gui.MainFrame
import objects.OBSClient
import java.awt.EventQueue
import java.util.logging.Logger

fun main(args: Array<String>) {
    val logger = Logger.getLogger("Application")
    logger.info("Starting application")
    logger.info("Executing JAR directory: " + getCurrentJarDirectory(Config).absolutePath)

    Config.load()
    Config.save()

    EventQueue.invokeLater {
        MainFrame()
    }

    OBSClient.start()
}