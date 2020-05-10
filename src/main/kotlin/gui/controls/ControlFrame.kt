package gui.controls

import config.Config
import gui.utils.loadIcon
import java.awt.BorderLayout
import java.awt.Component
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.util.logging.Logger
import javax.swing.JFrame
import javax.swing.JPanel

class ControlFrame(private val parentFrame: Component?) : JFrame() {
    private val logger = Logger.getLogger(ControlFrame::class.java.name)

    init {
        addWindowListener(object : WindowAdapter() {
            override fun windowClosing(winEvt: WindowEvent) {
                saveWindowPosition()
            }
        })

        createGui()
    }

    private fun createGui() {
        val mainPanel = JPanel(BorderLayout(10, 10))
        add(mainPanel)

        mainPanel.add(ControlFramePanel(), BorderLayout.CENTER)

        if (Config.windowRestoreLastPosition) {
            size = Config.controlWindowSize

            if (isLocationRelaviteToParent()) {
                setLocationRelativeTo(parentFrame)
            } else {
                location = Config.controlWindowLocation
            }
        } else {
            setSize(500, 250)
            setLocationRelativeTo(parentFrame)
        }

        title = "Live control"
        isVisible = true
        iconImage = loadIcon("/icon-512.png")
    }

    fun saveWindowPosition() {
        Config.controlWindowSize = size
        Config.controlWindowLocation = location
    }

    private fun isLocationRelaviteToParent() =
        Config.controlWindowLocation.x == -1 && Config.controlWindowLocation.y == -1
}