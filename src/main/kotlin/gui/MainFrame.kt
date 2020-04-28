package gui

import GUI
import exitApplication
import gui.menu.MenuBar
import gui.utils.loadIcon
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.util.logging.Logger
import javax.swing.JFrame

class MainFrame : JFrame(), Refreshable {
    private val logger = Logger.getLogger(MainFrame::class.java.name)

    init {
        GUI.register(this)

        addWindowListener(object : WindowAdapter() {
            override fun windowClosing(winEvt: WindowEvent) {
                exitApplication()
            }
        })

        initGUI()
    }

    private fun initGUI() {
        add(MainFramePanel())

        jMenuBar = MenuBar()
        setSize(1000, 600)
        title = "OBS Scene Que"
        defaultCloseOperation = EXIT_ON_CLOSE
        isVisible = true
        iconImage = loadIcon("/icon-512.png")
    }
}