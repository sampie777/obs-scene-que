package gui.updater

import config.Config
import objects.ApplicationInfo
import openWebURL
import themes.Theme
import java.awt.Dimension
import java.awt.Font
import java.awt.event.KeyEvent
import java.util.logging.Logger
import javax.swing.*
import javax.swing.border.EmptyBorder

class UpdatePopupActionPanel(private val frame: UpdatePopup) : JPanel() {
    private val logger = Logger.getLogger(UpdatePopupActionPanel::class.java.name)

    private val disableUpdateCheckerCheckbox = JCheckBox()

    init {
        createGui()
    }

    private fun createGui() {
        layout = BoxLayout(this, BoxLayout.LINE_AXIS)
        border = EmptyBorder(0, 10, 10, 10)

        disableUpdateCheckerCheckbox.text = "Don't check for future updates"
        disableUpdateCheckerCheckbox.toolTipText = "This can be re-enabled in the settings screen"
        disableUpdateCheckerCheckbox.font = Font(Theme.get.FONT_FAMILY, Font.PLAIN, 12)
        disableUpdateCheckerCheckbox.isSelected = !Config.updatesCheckForUpdates

        val closeButton = JButton("Close")
        closeButton.addActionListener { closePopup() }
        closeButton.mnemonic = KeyEvent.VK_C

        val downloadButton = JButton("Download")
        downloadButton.toolTipText = "The download website will be opened"
        downloadButton.addActionListener { downloadUpdate() }
        downloadButton.mnemonic = KeyEvent.VK_D
        frame.rootPane.defaultButton = downloadButton

        add(disableUpdateCheckerCheckbox)
        add(Box.createHorizontalGlue())
        add(closeButton)
        add(Box.createRigidArea(Dimension(10, 0)))
        add(downloadButton)
    }

    private fun closePopup() {
        logger.info("Closing update window")

        if (Config.updatesCheckForUpdates != !disableUpdateCheckerCheckbox.isSelected) {
            logger.info("Updating Config.updatesCheckForUpdates to ${!disableUpdateCheckerCheckbox.isSelected}")
        }
        Config.updatesCheckForUpdates = !disableUpdateCheckerCheckbox.isSelected

        frame.dispose()
    }

    private fun downloadUpdate() {
        logger.info("Opening update download website")

        openWebURL(ApplicationInfo.downloadsUrl, "Download update")

        closePopup()
    }
}
