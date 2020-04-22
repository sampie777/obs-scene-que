package gui

import GUI
import createImageIcon
import gui.notifications.NotificationFrame
import objects.notifications.Notifications
import java.awt.BorderLayout
import java.awt.Cursor
import java.awt.Dimension
import java.util.logging.Logger
import javax.swing.*
import javax.swing.border.EmptyBorder

class MainFramePanel : JSplitPane(), Refreshable {
    private val logger = Logger.getLogger(MainFramePanel::class.java.name)

    val notificationsButton = JButton()

    private val notificationsButtonIconDefault: Icon? = createImageIcon("/notification-bell-empty-24.png")
    private val notificationsButtonIconYellow: Icon? = createImageIcon("/notification-bell-yellow-24.png")

    init {
        GUI.register(this)

        createGui()

        refreshNotifications()
    }

    private fun createGui() {
        notificationsButton.isBorderPainted = false
        notificationsButton.isContentAreaFilled = false
        notificationsButton.isFocusPainted = false
        notificationsButton.cursor = Cursor(Cursor.HAND_CURSOR)
        notificationsButton.addActionListener {
            NotificationFrame(this)
        }

        val leftBottomPanel = JPanel(BorderLayout(10, 10))
        leftBottomPanel.border = EmptyBorder(0, 10, 10, 10)
        leftBottomPanel.minimumSize = Dimension(0, 0)
        leftBottomPanel.add(OBSStatusPanel(), BorderLayout.LINE_START)
        leftBottomPanel.add(notificationsButton, BorderLayout.LINE_END)

        val leftPanel = JPanel()
        leftPanel.layout = BorderLayout(10, 10)
        leftPanel.add(SceneListPanel(), BorderLayout.CENTER)
        leftPanel.add(leftBottomPanel, BorderLayout.PAGE_END)

        val rightPanel = JPanel()
        rightPanel.layout = BorderLayout(10, 10)
        rightPanel.add(SceneQuePanel(), BorderLayout.LINE_START)
        rightPanel.add(SceneLiveControlPanel(), BorderLayout.CENTER)

        setLeftComponent(leftPanel)
        setRightComponent(rightPanel)
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