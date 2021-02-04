package gui.updater

import gui.mainFrame.MainFrame
import gui.utils.DefaultDialogKeyDispatcher
import java.awt.BorderLayout
import java.awt.KeyboardFocusManager
import java.util.logging.Logger
import javax.swing.JDialog
import javax.swing.JFrame
import javax.swing.JPanel

class UpdatePopup(private val version: String, private val parentFrame: JFrame?) : JDialog(parentFrame) {
    private val logger = Logger.getLogger(UpdatePopup::class.java.name)

    companion object {
        fun create(version: String, parentFrame: JFrame? = MainFrame.getInstance()): UpdatePopup =
            UpdatePopup(version, parentFrame)

        fun createAndShow(version: String, parentFrame: JFrame? = MainFrame.getInstance()): UpdatePopup {
            val frame = create(version, parentFrame)
            frame.isVisible = true
            return frame
        }
    }

    init {
        KeyboardFocusManager
            .getCurrentKeyboardFocusManager()
            .addKeyEventDispatcher(DefaultDialogKeyDispatcher(this))

        createGui()
    }

    private fun createGui() {
        val mainPanel = JPanel(BorderLayout(10, 10))
        add(mainPanel)

        mainPanel.add(UpdatePopupContent(version), BorderLayout.CENTER)
        mainPanel.add(UpdatePopupActionPanel(this), BorderLayout.PAGE_END)

        title = "New update available"
        pack()
        setLocationRelativeTo(parentFrame)
        modalityType = ModalityType.APPLICATION_MODAL
    }
}
