package gui.menu

import exitApplication
import gui.MainFrame
import gui.config.ConfigFrame
import gui.controls.ControlFrame
import gui.notifications.NotificationFrame
import gui.utils.getMainFrameComponent
import gui.websocketScanner.WebsocketScannerFrame
import themes.Theme
import java.util.logging.Logger
import javax.swing.BorderFactory
import javax.swing.JMenu
import javax.swing.JMenuItem

class ApplicationMenu : JMenu("Application") {
    private val logger = Logger.getLogger(ApplicationMenu::class.java.name)

    init {
        initGui()
    }

    private fun initGui() {
        popupMenu.border = BorderFactory.createLineBorder(Theme.get.BORDER_COLOR)

        val controlFrameItem = JMenuItem("Control window")
        val notificationsItem = JMenuItem("Notifications")
        val scannerItem = JMenuItem("Network Scanner")
        val settingsItem = JMenuItem("Settings")
        val fullscreenItem = JMenuItem("Toggle fullscreen")
        val infoItem = JMenuItem("Info")
        val quitItem = JMenuItem("Quit")

        controlFrameItem.addActionListener { ControlFrame.createAndShow(getMainFrameComponent(this)) }
        notificationsItem.addActionListener { NotificationFrame(getMainFrameComponent(this)) }
        scannerItem.addActionListener { WebsocketScannerFrame(getMainFrameComponent(this)) }
        settingsItem.addActionListener { ConfigFrame(getMainFrameComponent(this)) }
        fullscreenItem.addActionListener {
            (getMainFrameComponent(this) as MainFrame).toggleFullscreen()
        }
        infoItem.addActionListener { InfoFrame.createAndShow(getMainFrameComponent(this)) }
        quitItem.addActionListener { exitApplication() }

        add(controlFrameItem)
        add(notificationsItem)
        add(scannerItem)
        add(settingsItem)
        addSeparator()
        add(fullscreenItem)
        add(infoItem)
        add(quitItem)
    }
}