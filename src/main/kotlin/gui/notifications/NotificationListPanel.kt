package gui.notifications

import GUI
import gui.Refreshable
import objects.notifications.Notifications
import java.awt.Dimension
import java.util.logging.Logger
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JLabel
import javax.swing.JPanel

class NotificationListPanel : JPanel(), Refreshable {

    private val logger = Logger.getLogger(NotificationListPanel::class.java.name)

    private val mainPanel = JPanel()

    init {
        GUI.register(this)

        createGui()

        Notifications.markAllAsRead()
    }

    private fun createGui() {
        mainPanel.layout = BoxLayout(mainPanel, BoxLayout.PAGE_AXIS)
        add(mainPanel)
    }

    override fun removeNotify() {
        super.removeNotify()
        GUI.unregister(this)
    }

    private fun addNotificationPanels() {
        mainPanel.removeAll()

        if (Notifications.list.size == 0) {
            mainPanel.add(Box.createRigidArea(Dimension(0, 70)))
            mainPanel.add(JLabel("No notifications"))
        } else {
            Notifications.list.stream()
                .sorted { notification, notification2 -> notification2.timestamp.compareTo(notification.timestamp) }
                .forEach {
                    mainPanel.add(NotificationPanel(it))
                }
        }

        repaint()
        revalidate()
    }

    override fun refreshNotifications() {
        addNotificationPanels()
    }
}