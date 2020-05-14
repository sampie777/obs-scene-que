package objects.notifications

import GUI
import java.util.logging.Logger

object Notifications {
    private val logger = Logger.getLogger(Notifications.toString())

    val list: ArrayList<Notification> = ArrayList()
    var unreadNotifications: Int = 0

    fun add(notification: Notification) {
        logger.info("Adding new notification: $notification")
        list.add(notification)
        unreadNotifications++

        GUI.refreshNotifications()
    }

    fun add(message: String, subject: String = "") {
        add(Notification(message, subject))
    }

    fun markAllAsRead() {
        logger.info("Marking all notifications as read")
        unreadNotifications = 0
        GUI.refreshNotifications()
    }

    fun clear() {
        list.clear()
        markAllAsRead()
    }
}