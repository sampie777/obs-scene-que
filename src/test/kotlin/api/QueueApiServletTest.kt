package api

import config.Config
import mocks.MockPlugin
import mocks.QueItemMock
import objects.ApplicationInfo
import objects.notifications.Notifications
import objects.que.Que
import org.eclipse.jetty.http.HttpStatus
import org.junit.AfterClass
import org.junit.BeforeClass
import plugins.PluginLoader
import java.net.ServerSocket
import kotlin.test.*


@Suppress("DEPRECATION")
class QueueApiServletTest {
    companion object {
        private var apiRootEndpoint: String = "/queue"
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

    private val mockPlugin = MockPlugin()

    @BeforeTest
    fun before() {
        PluginLoader.registerQueItemPlugin(mockPlugin)
        Que.clear()

        val item1 = mockPlugin.configStringToQueItem("1")
        val item2 = mockPlugin.configStringToQueItem("2")
        val item3 = mockPlugin.configStringToQueItem("3")
        Que.add(item1)
        Que.add(item2)
        Que.add(item3)

        Que.setCurrentQueItemByIndex(1)
        Notifications.clear()
    }

    @AfterTest
    fun after() {
        PluginLoader.unregisterQueItemPlugin(mockPlugin)
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
    fun testGetInvalidDeleteEndpoint() {
        val connection = delete("${apiUrl}/x")

        assertEquals(HttpStatus.NOT_FOUND_404, connection.responseCode)
        assertEquals("Not Found", connection.errorBody()?.trim())
    }

    @Test
    fun testGetList() {
        val connection = get("${apiUrl}/list")
        assertEquals(HttpStatus.OK_200, connection.responseCode)
        assertEquals(
            """{
  "data": {
    "name": "default-que",
    "applicationVersion": "${ApplicationInfo.version}",
    "queueItems": [
      {
        "pluginName": "MockPlugin",
        "className": "QueItemMock",
        "name": "1",
        "executeAfterPrevious": false,
        "quickAccessColor": null,
        "data": {}
      },
      {
        "pluginName": "MockPlugin",
        "className": "QueItemMock",
        "name": "2",
        "executeAfterPrevious": false,
        "quickAccessColor": null,
        "data": {}
      },
      {
        "pluginName": "MockPlugin",
        "className": "QueItemMock",
        "name": "3",
        "executeAfterPrevious": false,
        "quickAccessColor": null,
        "data": {}
      }
    ],
    "apiVersion": 1
  }
}""".trimIndent(), connection.body().trim()
        )
    }

    @Test
    fun testGetCurrent() {
        val connection = get("${apiUrl}/current")
        assertEquals(HttpStatus.OK_200, connection.responseCode)
        assertEquals(
            """{
  "data": {
    "pluginName": "MockPlugin",
    "className": "QueItemMock",
    "name": "2",
    "executeAfterPrevious": false,
    "quickAccessColor": null,
    "data": {}
  }
}""".trimIndent(), connection.body().trim()
        )
    }

    @Test
    fun testGetPrevious() {
        val connection = get("${apiUrl}/previous")
        assertEquals(HttpStatus.OK_200, connection.responseCode)
        assertEquals(
            """{
  "data": {
    "pluginName": "MockPlugin",
    "className": "QueItemMock",
    "name": "1",
    "executeAfterPrevious": false,
    "quickAccessColor": null,
    "data": {}
  }
}""".trimIndent(), connection.body().trim()
        )
    }

    @Test
    fun testGetNext() {
        val connection = get("${apiUrl}/next")
        assertEquals(HttpStatus.OK_200, connection.responseCode)
        assertEquals(
            """{
  "data": {
    "pluginName": "MockPlugin",
    "className": "QueItemMock",
    "name": "3",
    "executeAfterPrevious": false,
    "quickAccessColor": null,
    "data": {}
  }
}""".trimIndent(), connection.body().trim()
        )
    }

    @Test
    fun testGetIndex() {
        val connection = get("${apiUrl}/1")
        assertEquals(HttpStatus.OK_200, connection.responseCode)
        assertEquals(
            """{
  "data": {
    "pluginName": "MockPlugin",
    "className": "QueItemMock",
    "name": "2",
    "executeAfterPrevious": false,
    "quickAccessColor": null,
    "data": {}
  }
}""".trimIndent(), connection.body().trim()
        )
    }

    @Test
    fun testGetIndexNotFound() {
        val connection = get("${apiUrl}/10")
        assertEquals(HttpStatus.OK_200, connection.responseCode)
        assertEquals(
            """{
  "data": null
}""".trimMargin(), connection.body().trim()
        )
    }

    @Test
    fun testPostCurrent() {
        val queueItem = Que.current() as QueItemMock
        assertFalse(queueItem.isActivated)

        val connection = post("${apiUrl}/current")

        assertEquals(HttpStatus.OK_200, connection.responseCode)
        assertEquals(
            """{
  "data": {
    "pluginName": "MockPlugin",
    "className": "QueItemMock",
    "name": "2",
    "executeAfterPrevious": false,
    "quickAccessColor": null,
    "data": {}
  }
}""".trimIndent(), connection.body().trim()
        )
        assertTrue(queueItem.isActivated)
    }

    @Test
    fun testPostNext() {
        val queueItemCurrent = Que.current() as QueItemMock
        val queueItemNext = Que.previewNext() as QueItemMock
        assertFalse(queueItemCurrent.isActivated)
        assertFalse(queueItemCurrent.isDeactivated)
        assertFalse(queueItemNext.isActivated)

        val connection = post("${apiUrl}/next")

        assertEquals(HttpStatus.OK_200, connection.responseCode)
        assertEquals(
            """{
  "data": {
    "pluginName": "MockPlugin",
    "className": "QueItemMock",
    "name": "3",
    "executeAfterPrevious": false,
    "quickAccessColor": null,
    "data": {}
  }
}""".trimIndent(), connection.body().trim()
        )
        assertFalse(queueItemCurrent.isActivated)
        assertTrue(queueItemCurrent.isDeactivated)
        assertTrue(queueItemNext.isActivated)
        assertEquals(queueItemNext, Que.current())
    }

    @Test
    fun testPostIndex() {
        val queueItemCurrent = Que.current() as QueItemMock
        val queueItemNext = Que.previewNext() as QueItemMock
        assertFalse(queueItemCurrent.isActivated)
        assertFalse(queueItemCurrent.isDeactivated)
        assertFalse(queueItemNext.isActivated)

        assertNotEquals(2, Que.currentIndex())
        val connection = post("${apiUrl}/2")

        assertEquals(HttpStatus.OK_200, connection.responseCode)
        assertEquals(
            """{
  "data": {
    "pluginName": "MockPlugin",
    "className": "QueItemMock",
    "name": "3",
    "executeAfterPrevious": false,
    "quickAccessColor": null,
    "data": {}
  }
}""".trimIndent(), connection.body().trim()
        )
        assertEquals(2, Que.currentIndex())

        assertFalse(queueItemCurrent.isActivated)
        assertTrue(queueItemCurrent.isDeactivated)
        assertTrue(queueItemNext.isActivated)
        assertEquals(queueItemNext, Que.current())
    }

    @Test
    fun testPostAddAtEnd() {
        assertEquals(3, Que.size())

        val connection = post(
            "${apiUrl}/add",
            """
            {
              "pluginName": "MockPlugin",
              "className": "QueItemMock",
              "name": "4",
              "executeAfterPrevious": false,
    "quickAccessColor": null,
              "data": {}
            }
        """.trimIndent()
        )

        assertEquals(HttpStatus.OK_200, connection.responseCode)
        assertEquals(
            """{
  "data": {
    "pluginName": "MockPlugin",
    "className": "QueItemMock",
    "name": "4",
    "executeAfterPrevious": false,
    "quickAccessColor": null,
    "data": {}
  }
}""".trimIndent(), connection.body().trim()
        )
        assertEquals(4, Que.size())
        assertEquals(1, Que.currentIndex())
        val queItem = Que.getAt(3)!!
        assertEquals("MockPlugin", queItem.plugin.name)
        assertEquals("4", queItem.name)
    }

    @Test
    fun testPostAddAtIndex() {
        assertEquals(3, Que.size())

        val connection = post(
            "${apiUrl}/add/1",
            """
            {
              "pluginName": "MockPlugin",
              "className": "QueItemMock",
              "name": "4",
              "executeAfterPrevious": false,
              "data": {}
            }
        """.trimIndent()
        )

        assertEquals(HttpStatus.OK_200, connection.responseCode)
        assertEquals(
            """{
  "data": {
    "pluginName": "MockPlugin",
    "className": "QueItemMock",
    "name": "4",
    "executeAfterPrevious": false,
    "quickAccessColor": null,
    "data": {}
  }
}""".trimIndent(), connection.body().trim()
        )
        assertEquals(4, Que.size())
        assertEquals(2, Que.currentIndex())
        val queItem = Que.getAt(1)!!
        assertEquals("MockPlugin", queItem.plugin.name)
        assertEquals("4", queItem.name)
    }

    @Test
    fun testPostAddInvalidQueueItemPlugin() {
        assertEquals(3, Que.size())

        val connection = post(
            "${apiUrl}/add",
            """{
  "data": {
  "pluginName": "xx",
  "className": "QueItemMock",
  "name": "4",
  "executeAfterPrevious": false,
  "quickAccessColor": null,
  "data": {}
}""".trimIndent()
        )

        assertEquals(HttpStatus.OK_200, connection.responseCode)
        assertEquals(
            """{
  "data": null
}""".trimIndent(), connection.body().trim()
        )
        assertEquals(3, Que.size())
        assertEquals(1, Que.currentIndex())
        assertEquals(1, Notifications.unreadNotifications)
    }

    @Test
    fun testPostAddInvalidQueueItemBody() {
        assertEquals(3, Que.size())

        val connection = post(
            "${apiUrl}/add",
            "xx"
        )

        assertEquals(HttpStatus.OK_200, connection.responseCode)
        assertEquals(
            """{
  "data": null
}""".trimIndent(), connection.body().trim()
        )
        assertEquals(3, Que.size())
        assertEquals(1, Que.currentIndex())
        assertEquals(1, Notifications.unreadNotifications)
    }

    @Test
    fun testDeleteIndex() {
        assertEquals(3, Que.size())
        val connection = delete("${apiUrl}/1")

        assertEquals(HttpStatus.OK_200, connection.responseCode)
        assertEquals(
            """{
  "data": {
    "pluginName": "MockPlugin",
    "className": "QueItemMock",
    "name": "2",
    "executeAfterPrevious": false,
    "quickAccessColor": null,
    "data": {}
  }
}""".trimIndent(), connection.body().trim()
        )
        assertEquals(2, Que.size())
    }

    @Test
    fun testDeleteInvalidIndex() {
        assertEquals(3, Que.size())
        val connection = delete("${apiUrl}/10")

        assertEquals(HttpStatus.NOT_FOUND_404, connection.responseCode)
        assertEquals("Not Found", connection.errorBody()?.trim())
        assertEquals(3, Que.size())
    }
}