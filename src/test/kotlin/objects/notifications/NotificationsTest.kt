package objects.notifications

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals


class NotificationsTest {

    @BeforeTest
    fun before() {
        Notifications.clear()
    }

    @Test
    fun testClearNotifications() {
        Notifications.add("message", "subject")
        Notifications.add("message", "subject")
        assertEquals(2, Notifications.unreadNotifications)
        assertEquals(2, Notifications.list.size)

        Notifications.clear()

        assertEquals(0, Notifications.unreadNotifications)
        assertEquals(0, Notifications.list.size)
    }

    @Test
    fun testAddNotificationObject() {
        val notification = Notification("message", "subject")
        Notifications.add(notification)

        assertEquals(1, Notifications.unreadNotifications)
        assertEquals(1, Notifications.list.size)
        assertEquals("message", Notifications.list[0].message)
        assertEquals("subject", Notifications.list[0].subject)
        assertEquals(notification, Notifications.list[0])
    }

    @Test
    fun testAddNotificationUsingArguments() {
        Notifications.add("message", "subject")

        assertEquals(1, Notifications.unreadNotifications)
        assertEquals(1, Notifications.list.size)
        assertEquals("message", Notifications.list[0].message)
        assertEquals("subject", Notifications.list[0].subject)
    }

    @Test
    fun testAddNotificationWithoutNotifying() {
        Notifications.add("message", "subject", markAsRead = true)

        assertEquals(0, Notifications.unreadNotifications)
        assertEquals(1, Notifications.list.size)
    }
}