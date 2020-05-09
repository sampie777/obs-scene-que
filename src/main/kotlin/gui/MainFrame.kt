package gui

import GUI
import config.Config
import exitApplication
import gui.menu.MenuBar
import gui.utils.loadIcon
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.util.logging.Logger
import javax.swing.JFrame

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

        addWindowListener(object : WindowAdapter() {
            override fun windowClosing(winEvt: WindowEvent) {
                saveWindowPosition()
                exitApplication()
            }
        })

        initGUI()
    }

    fun saveWindowPosition() {
        Config.windowLocationX = location.x
        Config.windowLocationY = location.y
        Config.windowLocationWidth = size.width
        Config.windowLocationHeight = size.height
    }

    private fun initGUI() {
        add(MainFramePanel())

        if (Config.windowRestoreLastPosition) {
            setSize(Config.windowLocationWidth, Config.windowLocationHeight)
            setLocation(Config.windowLocationX, Config.windowLocationY)
        } else {
            setSize(1000, 600)
        }

        jMenuBar = MenuBar()
        title = "OBS Scene Que"
        defaultCloseOperation = EXIT_ON_CLOSE
        iconImage = loadIcon("/icon-512.png")
    }
}