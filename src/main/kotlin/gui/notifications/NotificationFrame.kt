package gui.notifications

import gui.utils.DefaultDialogKeyDispatcher
import java.awt.BorderLayout
import java.awt.Component
import java.awt.KeyboardFocusManager
import javax.swing.*

class NotificationFrame(private val parentFrame: JFrame?) : JDialog(parentFrame) {

    init {
        KeyboardFocusManager
            .getCurrentKeyboardFocusManager()
            .addKeyEventDispatcher(DefaultDialogKeyDispatcher(this))

        createGui()
    }

    private fun createGui() {
        val scrollPanel = JScrollPane(NotificationListPanel())
        scrollPanel.border = null

        val closeButton = JButton("Close")
        closeButton.horizontalAlignment = SwingConstants.CENTER
        closeButton.alignmentX = Component.CENTER_ALIGNMENT
        closeButton.addActionListener { dispose() }

        val actionPanel = JPanel()
        actionPanel.add(closeButton)

        val mainPanel = JPanel()
        mainPanel.layout = BorderLayout(0, 0)
        mainPanel.add(scrollPanel, BorderLayout.CENTER)
        mainPanel.add(actionPanel, BorderLayout.PAGE_END)
        add(mainPanel)

        title = "Notifications"
        setSize(423, 500)
        setLocationRelativeTo(parentFrame)
        modalityType = ModalityType.APPLICATION_MODAL
        isVisible = true
    }
}