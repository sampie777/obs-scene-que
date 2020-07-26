package objects.notifications

import java.util.*

data class Notification(val message: String, val subject: String = "") {
    val timestamp = Date()
}