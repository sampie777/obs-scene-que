package api


import com.google.gson.Gson
import objects.notifications.Notification
import objects.notifications.Notifications
import java.util.logging.Logger
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class NotificationApiServlet : HttpServlet() {
    private val logger = Logger.getLogger(ConfigApiServlet::class.java.name)

    operator fun Regex.contains(text: CharSequence?): Boolean = this.matches(text ?: "")

    override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        logger.info("Processing ${request.method} request from : ${request.requestURI}")

        when (request.pathInfo) {
            "/list" -> getList(request, response)
            "/last" -> getLast(response)
            else -> respondWithNotFound(response)
        }
    }

    override fun doPost(request: HttpServletRequest, response: HttpServletResponse) {
        logger.info("Processing ${request.method} request from : ${request.requestURI}")

        when (request.pathInfo) {
            "/markAllAsRead" -> postMarkAllAsRead(request, response)
            "/add" -> postAdd(request, response)
            else -> respondWithNotFound(response)
        }
    }

    private fun getList(request: HttpServletRequest, response: HttpServletResponse) {
        logger.info("Getting Notifications list")

        val notifications = if (request.getQueryParameter("unread", "false") == "true") {
            logger.info("Getting ${Notifications.unreadNotifications} unread notifications")
            Notifications.list.subList(
                Notifications.list.size - Notifications.unreadNotifications,
                Notifications.list.size
            )
        } else {
            Notifications.list
        }

        respondWithJson(response, notifications)
    }

    private fun getLast(response: HttpServletResponse) {
        logger.info("Getting last Notification")

        if (Notifications.list.isEmpty()) {
            respondWithJson(response, null)
            return
        }

        val notification = Notifications.list.last()

        respondWithJson(response, notification)
    }

    private fun postMarkAllAsRead(request: HttpServletRequest, response: HttpServletResponse) {
        logger.info("Marking all notifications as read")
        Notifications.markAllAsRead()
        response.status = HttpServletResponse.SC_OK
    }

    private fun postAdd(request: HttpServletRequest, response: HttpServletResponse) {
        logger.info("Adding new notification")

        val json = request.body()
        val notification = Gson().fromJson(json, Notification::class.java)
        logger.info(notification.toString())

        val markAsRead = request.getQueryParameter("markAsRead", "false") == "true"
        if (request.getQueryParameter("popup", "false") == "true") {
            Notifications.popup(notification, markAsRead = markAsRead)
        } else {
            Notifications.add(notification, markAsRead = markAsRead)
        }

        respondWithJson(response, notification)
    }
}