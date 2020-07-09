package gui.menu

import exitApplication
import gui.config.ConfigFrame
import gui.controls.ControlFrame
import gui.mainFrame.MainFrame
import gui.notifications.NotificationFrame
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
        controlFrameItem.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK or InputEvent.ALT_MASK)
        notificationsItem.mnemonic = KeyEvent.VK_N
        notificationsItem.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK or InputEvent.ALT_MASK)
        scannerItem.mnemonic = KeyEvent.VK_W
        scannerItem.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_MASK or InputEvent.ALT_MASK)
        settingsItem.mnemonic = KeyEvent.VK_S
        settingsItem.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK or InputEvent.ALT_MASK)
        fullscreenItem.mnemonic = KeyEvent.VK_F
        fullscreenItem.accelerator = KeyStroke.getKeyStroke("F11")
        infoItem.mnemonic = KeyEvent.VK_I
        infoItem.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_MASK or InputEvent.ALT_MASK)
        quitItem.mnemonic = KeyEvent.VK_Q
        quitItem.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK)

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