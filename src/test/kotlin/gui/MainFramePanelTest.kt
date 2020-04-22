package gui

import objects.notifications.Notifications
import kotlin.test.Test
import kotlin.test.assertEquals

class MainFramePanelTest {

    @Test
    fun testNotificationButtonChangesOnNewNotifications() {
        val panel = MainFramePanel()

        Notifications.markAllAsRead()
        assertEquals("", panel.notificationsButton.text)
        assertEquals("No new notifications", panel.notificationsButton.toolTipText)

        Notifications.add("message")
        assertEquals("(1)", panel.notificationsButton.text)
        assertEquals("New notifications available", panel.notificationsButton.toolTipText)

        Notifications.markAllAsRead()
        assertEquals("", panel.notificationsButton.text)
        assertEquals("No new notifications", panel.notificationsButton.toolTipText)
    }
}