package gui.utils

import themes.Theme
import java.awt.Cursor
import java.awt.Desktop
import java.io.IOException
import java.net.URI
import java.util.logging.Logger
import javax.swing.BorderFactory
import javax.swing.JButton
import javax.swing.SwingConstants


class ClickableLinkComponent(linkText: String, private val url: String) : JButton() {
    private val logger = Logger.getLogger(ClickableLinkComponent::class.java.name)

    init {
        text = linkText
        border = BorderFactory.createEmptyBorder()
        isBorderPainted = false
        isContentAreaFilled = false
        isFocusPainted = false
        background = null
        foreground = Theme.get.LINK_FONT_COLOR
        horizontalAlignment = SwingConstants.LEFT
        cursor = Cursor(Cursor.HAND_CURSOR)
        toolTipText = url

        addActionListener {
            if (!Desktop.isDesktopSupported()) {
                logger.warning("Cannot open link '$url': not supported by host")
            }
            try {
                Desktop.getDesktop().browse(URI(url))
            } catch (e: IOException) {
                logger.severe("Error during opening link '$url'")
                e.printStackTrace()
            }
        }
    }
}