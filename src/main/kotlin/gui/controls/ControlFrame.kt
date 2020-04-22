package gui.controls

import gui.config.ConfigActionPanel
import gui.config.ConfigEditPanel
import gui.config.ConfigFrame
import java.awt.BorderLayout
import java.awt.Component
import java.util.logging.Logger
import javax.swing.JFrame
import javax.swing.JPanel

class ControlFrame(private val parentFrame: Component?)  : JFrame() {
    private val logger = Logger.getLogger(ControlFrame::class.java.name)

    init {
        createGui()
    }

    private fun createGui() {
        val mainPanel = JPanel(BorderLayout(10, 10))
        add(mainPanel)

        mainPanel.add(ControlFramePanel(), BorderLayout.CENTER)

        title = "Live control"
        setSize(500, 250)
        setLocationRelativeTo(parentFrame)
        isVisible = true
    }
}