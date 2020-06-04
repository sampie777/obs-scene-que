package gui

import GUI
import config.Config
import exitApplication
import gui.menu.MenuBar
import gui.utils.loadIcon
import objects.ApplicationInfo
import objects.que.Que
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.util.logging.Logger
import javax.swing.JFrame

class MainFrameWindowAdapter(private val frame: MainFrame) : WindowAdapter() {
    override fun windowClosing(winEvt: WindowEvent) {
        frame.saveWindowPosition()
        GUI.windowClosing(frame)
        exitApplication()
    }

    override fun windowActivated(e: WindowEvent?) {
        super.windowActivated(e)
        GUI.currentFrame = frame
    }
}

class MainFrame : JFrame(), Refreshable {
    private val logger = Logger.getLogger(MainFrame::class.java.name)

    companion object {
        fun create(): MainFrame = MainFrame()

        fun createAndShow(): MainFrame {
            val frame = create()
            frame.isVisible = true
            return frame
        }
    }

    init {
        GUI.register(this)

        addWindowListener(MainFrameWindowAdapter(this))

        initGUI()
    }

    private fun initGUI() {
        add(MainFramePanel())

        if (Config.windowRestoreLastPosition) {
            location = Config.mainWindowLocation
            size = Config.mainWindowSize

            if (Config.mainWindowsIsMaximized) {
                extendedState = extendedState or MAXIMIZED_BOTH
            }
        } else {
            setSize(1000, 600)
        }

        jMenuBar = MenuBar()
        title = ApplicationInfo.name + " - " + Que.name
        defaultCloseOperation = EXIT_ON_CLOSE
        iconImage = loadIcon("/icon-512.png")
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
}