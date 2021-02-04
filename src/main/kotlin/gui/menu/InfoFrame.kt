package gui.menu

import LogService
import config.Config
import gui.utils.ClickableLinkComponent
import gui.utils.DefaultDialogKeyDispatcher
import objects.ApplicationInfo
import themes.Theme
import java.awt.Dimension
import java.awt.Font
import java.awt.KeyboardFocusManager
import javax.swing.*
import javax.swing.border.EmptyBorder

class InfoFrame(private val parentFrame: JFrame?) : JDialog(parentFrame) {

    companion object {
        fun create(parentFrame: JFrame?): InfoFrame = InfoFrame(parentFrame)

        fun createAndShow(parentFrame: JFrame?): InfoFrame {
            val frame = create(parentFrame)
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
        val mainPanel = JPanel()
        mainPanel.layout = BoxLayout(mainPanel, BoxLayout.PAGE_AXIS)
        mainPanel.border = EmptyBorder(0, 20, 10, 20)
        add(mainPanel)

        val versionLabel = JLabel(
            "<html>" +
                    "<h1>${ApplicationInfo.name}</h1>" +
                    "<p>By ${ApplicationInfo.author}</p>" +
                    "<p>Version: ${ApplicationInfo.version}</p>" +
                    "</html>"
        )
        versionLabel.font = Font(Theme.get.FONT_FAMILY, Font.PLAIN, 14)

        val sourceCodeLabel = ClickableLinkComponent("Source code (GitHub)", ApplicationInfo.url)
        sourceCodeLabel.font = Font(Theme.get.FONT_FAMILY, Font.PLAIN, 14)

        val donationLabel = ClickableLinkComponent("Donate to ${ApplicationInfo.name}", ApplicationInfo.donationUrl)
        donationLabel.font = Font(Theme.get.FONT_FAMILY, Font.PLAIN, 14)

        val applicationLoggingInfoLabel = JLabel("<html>Application log file location: ${LogService.getLogFile()?.absolutePath}</html>")
        applicationLoggingInfoLabel.font = Font(Theme.get.FONT_FAMILY, Font.ITALIC, 12)

        mainPanel.add(versionLabel)
        mainPanel.add(Box.createRigidArea(Dimension(0, 10)))
        mainPanel.add(sourceCodeLabel)
        mainPanel.add(Box.createRigidArea(Dimension(0, 10)))
        mainPanel.add(donationLabel)
        mainPanel.add(Box.createRigidArea(Dimension(0, 20)))
        if (Config.enableApplicationLoggingToFile) {
            mainPanel.add(applicationLoggingInfoLabel)
        }

        title = "Information"
        pack()
        setLocationRelativeTo(parentFrame)
        modalityType = ModalityType.APPLICATION_MODAL
    }
}