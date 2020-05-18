package gui.controls

import GUI
import config.Config
import gui.utils.loadIcon
import java.awt.BorderLayout
import java.awt.Component
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.util.logging.Logger
import javax.swing.JFrame
import javax.swing.JPanel

class ControlFrameWindowAdapter(private val frame: ControlFrame) : WindowAdapter() {
    override fun windowClosing(winEvt: WindowEvent) {
        GUI.windowClosing(frame)
        frame.saveWindowPosition()
    }

    override fun windowActivated(e: WindowEvent?) {
        super.windowActivated(e)
        GUI.currentFrame = frame
    }
}

class ControlFrame(private val parentFrame: Component?) : JFrame() {
    private val logger = Logger.getLogger(ControlFrame::class.java.name)

    companion object {
        fun create(parentFrame: Component?): ControlFrame = ControlFrame(parentFrame)

        fun createAndShow(parentFrame: Component?): ControlFrame {
            val frame = create(parentFrame)
            frame.isVisible = true
            return frame
        }
    }

    init {
        addWindowListener(ControlFrameWindowAdapter(this))

        createGui()
    }

    private fun createGui() {
        val mainPanel = JPanel(BorderLayout(10, 10))
        add(mainPanel)

        mainPanel.add(ControlFramePanel(), BorderLayout.CENTER)

        if (Config.windowRestoreLastPosition) {
            if (isLocationRelaviteToParent()) {
                setLocationRelativeTo(parentFrame)
            } else {
                location = Config.controlWindowLocation
            }

            size = Config.controlWindowSize
            
            if (Config.controlWindowsIsMaximized) {
                extendedState = extendedState or MAXIMIZED_BOTH
            }
        } else {
            setSize(500, 250)
            setLocationRelativeTo(parentFrame)
        }

        title = "Live control"
        iconImage = loadIcon("/icon-512.png")
    }

    fun saveWindowPosition() {
        Config.controlWindowLocation = location

        if (extendedState == MAXIMIZED_BOTH) {
            Config.controlWindowsIsMaximized = true
        } else {
            Config.controlWindowsIsMaximized = false
            Config.controlWindowSize = size
        }
    }

    private fun isLocationRelaviteToParent() =
        Config.controlWindowLocation.x == -1 && Config.controlWindowLocation.y == -1
}