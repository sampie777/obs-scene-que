package gui.config

import java.awt.BorderLayout
import java.awt.Component
import java.util.logging.Logger
import javax.swing.JFrame
import javax.swing.JPanel

class ConfigFrame(private val parentFrame: Component?) : JFrame() {
    private val logger = Logger.getLogger(ConfigFrame::class.java.name)

    private val configEditPanel: ConfigEditPanel = ConfigEditPanel()

    init {
        createGui()
    }

    private fun createGui() {
        val mainPanel = JPanel(BorderLayout(10, 10))
        add(mainPanel)

        mainPanel.add(configEditPanel, BorderLayout.CENTER)
        mainPanel.add(ConfigActionPanel(this), BorderLayout.PAGE_END)

        title = "Settings"
        setSize(560, 520)
        setLocationRelativeTo(parentFrame)
        isVisible = true
    }

    fun saveAll(): Boolean {
        return configEditPanel.saveAll()
    }
}