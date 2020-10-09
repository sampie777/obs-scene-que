package gui.mainFrame

import GUI
import config.Config
import gui.Refreshable
import gui.globalHooks.GlobalKeyboardHook
import gui.menu.MenuBar
import gui.utils.loadIcon
import objects.ApplicationInfo
import objects.notifications.Notifications
import objects.que.Que
import java.awt.EventQueue
import java.util.logging.Logger
import javax.swing.JFrame


class MainFrame : JFrame(), Refreshable {
    private val logger = Logger.getLogger(MainFrame::class.java.name)

    companion object {
        private var instance: MainFrame? = null
        fun getInstance() = instance

        fun create(): MainFrame = MainFrame()

        fun createAndShow(): MainFrame {
            val frame = create()
            frame.isVisible = true
            return frame
        }
    }

    init {
        instance = this

        GUI.register(this)

        addWindowListener(MainFrameWindowAdapter(this))

        initGUI()

        GlobalKeyboardHook.register()
    }

    private fun initGUI() {
        add(MainFramePanel())

        if (Config.windowRestoreLastPosition) {
            location = Config.mainWindowLocation
            size = Config.mainWindowSize

            if (Config.mainWindowsIsMaximized) {
                extendedState = extendedState or MAXIMIZED_BOTH
            }

            setFullscreen(Config.mainWindowsIsFullscreen)
        } else {
            setSize(1300,760)
        }

        jMenuBar = MenuBar()
        defaultCloseOperation = EXIT_ON_CLOSE
        iconImage = loadIcon("/icon-512.png")
        refreshQueueName()
    }

    fun rebuildGui() {
        logger.info("Rebuilding main GUI")
        EventQueue.invokeLater {
            contentPane.removeAll()

            add(MainFramePanel())
            jMenuBar = MenuBar()

            revalidate()
            repaint()
            logger.info("GUI rebuild done")
        }
    }

    override fun refreshQueueName() {
        title = ApplicationInfo.name + " - " + Que.name
    }

    fun saveWindowPosition() {
        Config.mainWindowLocation = location

        if (extendedState == MAXIMIZED_BOTH) {
            Config.mainWindowsIsMaximized = true
        } else {
            Config.mainWindowsIsMaximized = false
            Config.mainWindowSize = size
        }
    }

    fun toggleFullscreen() {
        Config.mainWindowsIsFullscreen = !Config.mainWindowsIsFullscreen

        setFullscreen(Config.mainWindowsIsFullscreen)
    }

    private fun setFullscreen(value: Boolean) {
        val graphicsDevice = graphicsConfiguration.device

        if (value) {
            logger.info("Enabling fullscreen")
            if (!graphicsDevice.isFullScreenSupported) {
                logger.info("Fullscreen not supported on this graphics device: $graphicsDevice")
                Notifications.add("Fullscreen is not supported by your graphics device", "GUI")
                return
            }

            graphicsDevice.fullScreenWindow = this
        } else {
            logger.info("Disabling fullscreen")
            if (graphicsDevice.fullScreenWindow == this) {
                graphicsDevice.fullScreenWindow = null
            }
        }
    }
}