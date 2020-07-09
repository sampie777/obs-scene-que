package gui.plugins.config

import gui.utils.DefaultDialogKeyDispatcher
import java.awt.BorderLayout
import java.awt.KeyboardFocusManager
import java.util.logging.Logger
import javax.swing.JDialog
import javax.swing.JFrame
import javax.swing.JPanel

class ConfigWindow(
    private val parentFrame: JFrame?, title: String,
    private val editPanel: PluginConfigEditPanel,
    private val actionPanel: PluginConfigActionPanel? = null
) : JDialog(parentFrame, title) {

    private val logger = Logger.getLogger(ConfigWindow::class.java.name)

    init {
        KeyboardFocusManager
            .getCurrentKeyboardFocusManager()
            .addKeyEventDispatcher(DefaultDialogKeyDispatcher(this))

        createGui()
    }

    private fun createGui() {
        val mainPanel = JPanel(BorderLayout(10, 10))
        add(mainPanel)

        mainPanel.add(editPanel.create(), BorderLayout.CENTER)
        val configActionPanel = actionPanel ?: ConfigActionPanel(this)
        configActionPanel.frame = this
        mainPanel.add(configActionPanel, BorderLayout.PAGE_END)

        pack()
        setLocationRelativeTo(parentFrame)
        modalityType = ModalityType.APPLICATION_MODAL
        isVisible = true
    }

    fun saveAll(): Boolean {
        return editPanel.saveAll()
    }
}