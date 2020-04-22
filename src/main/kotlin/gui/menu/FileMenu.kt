package gui.menu

import exitApplication
import gui.config.ConfigFrame
import gui.notifications.NotificationFrame
import gui.utils.getMainFrameComponent
import java.util.logging.Logger
import javax.swing.JMenu
import javax.swing.JMenuItem

class FileMenu : JMenu("File") {
    private val logger = Logger.getLogger(FileMenu::class.java.name)

    init {
        initGui()
    }

    private fun initGui() {
        val notificationsItem = JMenuItem("Notifications")
        val settingsItem = JMenuItem("Settings")
        val infoItem = JMenuItem("Info")
        val quitItem = JMenuItem("Quit")

        notificationsItem.addActionListener { NotificationFrame(getMainFrameComponent(this)) }
        settingsItem.addActionListener { ConfigFrame(getMainFrameComponent(this)) }
        infoItem.addActionListener { InfoFrame(getMainFrameComponent(this)) }
        quitItem.addActionListener { exitApplication() }

        add(notificationsItem)
        add(settingsItem)
        add(infoItem)
        add(quitItem)
    }
}