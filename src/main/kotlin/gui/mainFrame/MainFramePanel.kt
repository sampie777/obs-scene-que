package gui.mainFrame

import GUI
import config.Config
import gui.OBSStatusPanel
import gui.Refreshable
import gui.RightMainPanel
import gui.SourcesPanel
import gui.list.QuePanel
import gui.notifications.NotificationFrame
import gui.utils.createImageIcon
import gui.utils.divider
import gui.utils.getMainFrameComponent
import objects.notifications.Notifications
import themes.Theme
import java.awt.BorderLayout
import java.awt.Component
import java.awt.Cursor
import java.awt.Dimension
import java.util.logging.Logger
import javax.swing.*
import javax.swing.border.EmptyBorder


class MainFramePanel : JSplitPane(), Refreshable {
    private val logger = Logger.getLogger(MainFramePanel::class.java.name)

    val notificationsButton = JButton()
    private val rightPanel = JSplitPane()

    private val notificationsButtonIconDefault: Icon? = createImageIcon(Theme.get.NOTIFICATIONS_BUTTON_ICON_DEFAULT)
    private val notificationsButtonIconYellow: Icon? = createImageIcon(Theme.get.NOTIFICATIONS_BUTTON_ICON_ALERT)

    init {
        GUI.register(this)

        createGui()

        refreshNotifications()
    }

    private fun createGui() {
        border = null
        val divider = divider()
        divider.border = BorderFactory.createMatteBorder(0, 0, 0, 1, Theme.get.BORDER_COLOR)
        divider.dividerSize = 6

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

        val leftPanel = JPanel(BorderLayout(10, 10))
        leftPanel.minimumSize = Dimension(10, 10)
        leftPanel.add(SourcesPanel(), BorderLayout.CENTER)
        leftPanel.add(leftBottomPanel, BorderLayout.PAGE_END)

        rightPanel.border = null
        rightPanel.leftComponent = QuePanel()
        rightPanel.leftComponent.minimumSize = Dimension(10, 10)
        rightPanel.rightComponent = RightMainPanel()
        rightPanel.rightComponent.minimumSize = Dimension(10, 10)
        val rightPanelDivider = rightPanel.divider()
        rightPanelDivider.border = BorderFactory.createMatteBorder(0, 0, 0, 1, Theme.get.BORDER_COLOR)
        rightPanelDivider.dividerSize = 7

        setLeftComponent(leftPanel)
        setRightComponent(rightPanel)

        if (Config.windowRestoreLastPosition) {
            dividerLocation = Config.mainPanelDividerLeftLocation
            rightPanel.dividerLocation = Config.mainPanelDividerRightLocation
        }
    }

    override fun removeNotify() {
        super.removeNotify()
        GUI.unregister(this)
    }

    override fun windowClosing(window: Component?) {
        Config.mainPanelDividerLeftLocation = dividerLocation
        Config.mainPanelDividerRightLocation = rightPanel.dividerLocation
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