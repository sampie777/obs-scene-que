package gui.menu

import exitApplication
import gui.HotKeysMapping
import gui.config.ConfigFrame
import gui.controls.ControlFrame
import gui.mainFrame.MainFrame
import gui.notifications.NotificationFrame
import gui.utils.addHotKeyMapping
import gui.utils.getMainFrameComponent
import gui.websocketScanner.WebsocketScannerFrame
import themes.Theme
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import java.util.logging.Logger
import javax.swing.BorderFactory
import javax.swing.JMenu
import javax.swing.JMenuItem
import javax.swing.KeyStroke

class ApplicationMenu : JMenu("Application") {
    private val logger = Logger.getLogger(ApplicationMenu::class.java.name)

    init {
        initGui()
    }

    private fun initGui() {
        popupMenu.border = BorderFactory.createLineBorder(Theme.get.BORDER_COLOR)
        addHotKeyMapping(HotKeysMapping.APPLICATION_MENU)

        val controlFrameItem = JMenuItem("Control window")
        val notificationsItem = JMenuItem("Notifications")
        val scannerItem = JMenuItem("Network Scanner")
        val settingsItem = JMenuItem("Settings")
        val fullscreenItem = JMenuItem("Toggle fullscreen")
        val infoItem = JMenuItem("Info")
        val quitItem = JMenuItem("Quit")

        // Set alt keys
        controlFrameItem.addHotKeyMapping(HotKeysMapping.CONTROL_FRAME_ITEM)
        notificationsItem.addHotKeyMapping(HotKeysMapping.NOTIFICATIONS_ITEM)
        scannerItem.addHotKeyMapping(HotKeysMapping.SCANNER_ITEM)
        settingsItem.addHotKeyMapping(HotKeysMapping.SETTINGS_ITEM)
        fullscreenItem.addHotKeyMapping(HotKeysMapping.FULLSCREEN_ITEM)
        fullscreenItem.accelerator = KeyStroke.getKeyStroke("F11")
        infoItem.addHotKeyMapping(HotKeysMapping.INFO_ITEM)
        quitItem.addHotKeyMapping(HotKeysMapping.QUIT_ITEM)
        quitItem.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK)

        add(controlFrameItem)
        add(notificationsItem)
        add(scannerItem)
        add(settingsItem)
        addSeparator()
        add(fullscreenItem)
        add(infoItem)
        add(quitItem)

        controlFrameItem.addActionListener { ControlFrame.createAndShow(getMainFrameComponent(this)) }
        notificationsItem.addActionListener { NotificationFrame(getMainFrameComponent(this)) }
        scannerItem.addActionListener { WebsocketScannerFrame(getMainFrameComponent(this)) }
        settingsItem.addActionListener { ConfigFrame(getMainFrameComponent(this)) }
        fullscreenItem.addActionListener {
            (getMainFrameComponent(this) as MainFrame).toggleFullscreen()
        }
        infoItem.addActionListener { InfoFrame.createAndShow(getMainFrameComponent(this)) }
        quitItem.addActionListener { exitApplication() }
    }
}