package objects.notifications

import java.util.*

class Notification(val message: String, val subject: String = "") {
    val timestamp = Date()

    override fun toString(): String {
        return "[Notification: subject=$subject; message=$message; timestamp=$timestamp]"
    }
}