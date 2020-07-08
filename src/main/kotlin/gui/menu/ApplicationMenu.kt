package gui.menu

import exitApplication
import gui.config.ConfigFrame
import gui.controls.ControlFrame
import gui.mainFrame.MainFrame
import gui.notifications.NotificationFrame
import gui.utils.getMainFrameComponent
import gui.websocketScanner.WebsocketScannerFrame
import themes.Theme
import java.awt.event.KeyEvent
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
        mnemonic = KeyEvent.VK_A

        val controlFrameItem = JMenuItem("Control window")
        val notificationsItem = JMenuItem("Notifications")
        val scannerItem = JMenuItem("Network Scanner")
        val settingsItem = JMenuItem("Settings")
        val fullscreenItem = JMenuItem("Toggle fullscreen")
        val infoItem = JMenuItem("Info")
        val quitItem = JMenuItem("Quit")

        // Set alt keys
        controlFrameItem.mnemonic = KeyEvent.VK_C
        notificationsItem.mnemonic = KeyEvent.VK_N
        scannerItem.mnemonic = KeyEvent.VK_W
        settingsItem.mnemonic = KeyEvent.VK_S
        fullscreenItem.mnemonic = KeyEvent.VK_F
        infoItem.mnemonic = KeyEvent.VK_I
        quitItem.mnemonic = KeyEvent.VK_Q

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