package gui

import GUI
import config.Config
import gui.notifications.NotificationFrame
import gui.utils.createImageIcon
import gui.utils.getMainFrameComponent
import objects.notifications.Notifications
import themes.Theme
import java.awt.BorderLayout
import java.awt.Component
import java.awt.Cursor
import java.awt.Dimension
import java.util.logging.Logger
import javax.swing.Icon
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.JSplitPane
import javax.swing.border.EmptyBorder

class MainFramePanel : JSplitPane(), Refreshable {
    private val logger = Logger.getLogger(MainFramePanel::class.java.name)

    val notificationsButton = JButton()

    private val notificationsButtonIconDefault: Icon? = createImageIcon(Theme.get.NOTIFICATIONS_BUTTON_ICON_DEFAULT)
    private val notificationsButtonIconYellow: Icon? = createImageIcon(Theme.get.NOTIFICATIONS_BUTTON_ICON_ALERT)

    init {
        GUI.register(this)

        createGui()

        refreshNotifications()
    }

    private fun createGui() {
        border = null

        notificationsButton.isBorderPainted = false
        notificationsButton.isContentAreaFilled = false
        notificationsButton.isFocusPainted = false
        notificationsButton.cursor = Cursor(Cursor.HAND_CURSOR)
        notificationsButton.addActionListener {
            NotificationFrame(getMainFrameComponent(this))
        }

        val leftBottomPanel = JPanel(BorderLayout(10, 10))
        leftBottomPanel.border = EmptyBorder(0, 10, 10, 10)
        leftBottomPanel.minimumSize = Dimension(0, 0)
        leftBottomPanel.add(OBSStatusPanel(), BorderLayout.LINE_START)
        leftBottomPanel.add(notificationsButton, BorderLayout.LINE_END)

        val leftPanel = JPanel()
        leftPanel.layout = BorderLayout(10, 10)
        leftPanel.add(SourcesPanel(), BorderLayout.CENTER)
        leftPanel.add(leftBottomPanel, BorderLayout.PAGE_END)

        val rightPanel = JPanel()
        rightPanel.layout = BorderLayout(10, 10)
        rightPanel.add(SceneQuePanel(), BorderLayout.LINE_START)
        rightPanel.add(SceneLiveControlPanel(), BorderLayout.CENTER)

        setLeftComponent(leftPanel)
        setRightComponent(rightPanel)

        if (Config.windowRestoreLastPosition) {
            dividerLocation = Config.mainPanelDividerLocation
        }
    }

    override fun removeNotify() {
        super.removeNotify()
        GUI.unregister(this)
    }

    override fun windowClosing(window: Component?) {
        Config.mainPanelDividerLocation = dividerLocation
    }

    override fun refreshNotifications() {
        if (Notifications.unreadNotifications > 0) {
            notificationsButton.icon = notificationsButtonIconYellow
            notificationsButton.text = "(${Notifications.unreadNotifications})"
            notificationsButton.toolTipText = "New notifications available"
            return
        }

        notificationsButton.icon = notificationsButtonIconDefault
        notificationsButton.text = ""
        notificationsButton.toolTipText = "No new notifications"
    }
}