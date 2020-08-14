package api

import config.Config
import format
import objects.notifications.Notifications
import org.eclipse.jetty.http.HttpStatus
import org.junit.AfterClass
import org.junit.BeforeClass
import java.net.ServerSocket
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals


class NotificationApiServletTest {
    companion object {
        private var apiRootEndpoint: String = "/notifications"
        private var apiUrl: String = "" + apiRootEndpoint

        @BeforeClass
        @JvmStatic
        fun setup() {
            // Get some random free port
            Config.httpApiServerPort = ServerSocket(0).use { it.localPort }

            ApiServer.start()
            apiUrl = ApiServer.uri().toString() + apiRootEndpoint
        }

        @AfterClass
        @JvmStatic
        fun tearDown() {
            ApiServer.stop()
        }
    }

    @BeforeTest
    fun before() {
        Notifications.clear()
        Notifications.add("message 1", "title 1")
        Notifications.markAllAsRead()
        Notifications.add("message 2", "title 2")
        Notifications.add("message 3", "title 3")
    }

    @Test
    fun testGetList() {
        val connection = get("${apiUrl}/list")

        assertEquals(HttpStatus.OK_200, connection.responseCode)
        assertEquals(
            """{
  "data": [
    {
      "timestamp": "${Notifications.list[0].timestamp.format("yyyy-MM-dd'T'HH:mm:ss.SSSZ")}",
      "message": "message 1",
      "subject": "title 1"
    },
    {
      "timestamp": "${Notifications.list[1].timestamp.format("yyyy-MM-dd'T'HH:mm:ss.SSSZ")}",
      "message": "message 2",
      "subject": "title 2"
    },
    {
      "timestamp": "${Notifications.list[2].timestamp.format("yyyy-MM-dd'T'HH:mm:ss.SSSZ")}",
      "message": "message 3",
      "subject": "title 3"
    }
  ]
}""".trimIndent(), connection.body().trim()
        )
    }

    @Test
    fun testGetEmptyList() {
        Notifications.clear()
        val connection = get("${apiUrl}/list")

        assertEquals(HttpStatus.OK_200, connection.responseCode)
        assertEquals("""{
  "data": []
}""", connection.body().trim())
    }

    @Test
    fun testGetUnreadList() {
        val connection = get("${apiUrl}/list?unread=true")

        assertEquals(HttpStatus.OK_200, connection.responseCode)
        assertEquals(
            """{
  "data": [
    {
      "timestamp": "${Notifications.list[1].timestamp.format("yyyy-MM-dd'T'HH:mm:ss.SSSZ")}",
      "message": "message 2",
      "subject": "title 2"
    },
    {
      "timestamp": "${Notifications.list[2].timestamp.format("yyyy-MM-dd'T'HH:mm:ss.SSSZ")}",
      "message": "message 3",
      "subject": "title 3"
    }
  ]
}""".trimIndent(), connection.body().trim()
        )
    }

    @Test
    fun testGetLast() {
        val connection = get("${apiUrl}/last")

        assertEquals(HttpStatus.OK_200, connection.responseCode)
        assertEquals(
            """{
  "data": {
    "timestamp": "${Notifications.list[2].timestamp.format("yyyy-MM-dd'T'HH:mm:ss.SSSZ")}",
    "message": "message 3",
    "subject": "title 3"
  }
}""".trimIndent(), connection.body().trim()
        )
    }

    @Test
    fun testGetLastWithNoNotifications() {
        Notifications.clear()
        val connection = get("${apiUrl}/last")

        assertEquals(HttpStatus.OK_200, connection.responseCode)
        assertEquals(
            """{
  "data": null
}""".trimIndent(), connection.body().trim()
        )
    }

    @Test
    fun testGetInvalidGetEndpoint() {
        val connection = get("${apiUrl}/x")

        assertEquals(HttpStatus.NOT_FOUND_404, connection.responseCode)
        assertEquals("Not Found", connection.errorBody()?.trim())
    }

    @Test
    fun testGetInvalidPostEndpoint() {
        val connection = post("${apiUrl}/x")

        assertEquals(HttpStatus.NOT_FOUND_404, connection.responseCode)
        assertEquals("Not Found", connection.errorBody()?.trim())
    }

    @Test
    fun testPostMarkAllAsRead() {
        assertNotEquals(0, Notifications.unreadNotifications)
        val connection = post("${apiUrl}/markAllAsRead")

        assertEquals(HttpStatus.OK_200, connection.responseCode)
        assertEquals(0, Notifications.unreadNotifications)
        assertNotEquals(0, Notifications.list.size)
    }

    @Test
    fun testPostAddUnreadNotification() {
        Notifications.clear()

        val connection = post(
            "${apiUrl}/add",
            """
            {
                "timestamp": "2020-07-26T13:38:27.046+0200",
                "message": "message new",
                "subject": "title new"
            }
        """.trimIndent()
        )

        assertEquals(HttpStatus.OK_200, connection.responseCode)
        assertEquals(
            """{
  "data": {
    "timestamp": "2020-07-26T13:38:27.046+0200",
    "message": "message new",
    "subject": "title new"
  }
}""".trimIndent(), connection.body().trim()
        )
        assertEquals(1, Notifications.list.size)
        assertEquals(1, Notifications.unreadNotifications)
        assertEquals("message new", Notifications.list[0].message)
        assertEquals("title new", Notifications.list[0].subject)
        assertEquals("Sun Jul 26 13:38:27 CEST 2020", Notifications.list[0].timestamp.toString())
    }

    @Test
    fun testPostAddReadNotification() {
        Notifications.clear()

        val connection = post(
            "${apiUrl}/add?markAsRead=true",
            """
            {
                "timestamp": "2020-07-26T13:38:27.046+0200",
                "message": "message new",
                "subject": "title new"
            }
        """.trimIndent()
        )

        assertEquals(HttpStatus.OK_200, connection.responseCode)
        assertEquals(
            """{
  "data": {
    "timestamp": "2020-07-26T13:38:27.046+0200",
    "message": "message new",
    "subject": "title new"
  }
}""".trimIndent(), connection.body().trim()
        )
        assertEquals(1, Notifications.list.size)
        assertEquals(0, Notifications.unreadNotifications)
        assertEquals("message new", Notifications.list[0].message)
        assertEquals("title new", Notifications.list[0].subject)
        assertEquals("Sun Jul 26 13:38:27 CEST 2020", Notifications.list[0].timestamp.toString())
    }
}