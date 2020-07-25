package api

import config.Config
import mocks.MockPlugin
import mocks.QueItemMock
import objects.que.Que
import org.eclipse.jetty.http.HttpStatus
import org.junit.AfterClass
import org.junit.BeforeClass
import java.net.HttpURLConnection
import java.net.ServerSocket
import java.net.URL
import kotlin.test.*


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
        Que.clear()

        val item1 = mockPlugin.configStringToQueItem("1")
        val item2 = mockPlugin.configStringToQueItem("2")
        val item3 = mockPlugin.configStringToQueItem("3")
        Que.add(item1)
        Que.add(item2)
        Que.add(item3)

        Que.setCurrentQueItemByIndex(1)
    }

    @Test
    fun testGetList() {
        val connection = URL("${apiUrl}/list").openConnection() as HttpURLConnection
        connection.connect()
        assertEquals(HttpStatus.OK_200, connection.responseCode)
        assertEquals("""{
  "name": "default-que",
  "applicationVersion": "2.6.0-SNAPSHOT-6",
  "queueItems": [
    {
      "pluginName": "MockPlugin",
      "className": "QueItemMock",
      "name": "1",
      "executeAfterPrevious": false,
      "data": {}
    },
    {
      "pluginName": "MockPlugin",
      "className": "QueItemMock",
      "name": "2",
      "executeAfterPrevious": false,
      "data": {}
    },
    {
      "pluginName": "MockPlugin",
      "className": "QueItemMock",
      "name": "3",
      "executeAfterPrevious": false,
      "data": {}
    }
  ],
  "apiVersion": 1
}""".trimIndent(), connection.body().trim())
    }

    @Test
    fun testGetCurrent() {
        val connection = URL("${apiUrl}/current").openConnection() as HttpURLConnection
        connection.connect()
        assertEquals(HttpStatus.OK_200, connection.responseCode)
        assertEquals("""{
  "pluginName": "MockPlugin",
  "className": "QueItemMock",
  "name": "2",
  "executeAfterPrevious": false,
  "data": {}
}""".trimIndent(), connection.body().trim())
    }

    @Test
    fun testGetPrevious() {
        val connection = URL("${apiUrl}/previous").openConnection() as HttpURLConnection
        connection.connect()
        assertEquals(HttpStatus.OK_200, connection.responseCode)
        assertEquals("""{
  "pluginName": "MockPlugin",
  "className": "QueItemMock",
  "name": "1",
  "executeAfterPrevious": false,
  "data": {}
}""".trimIndent(), connection.body().trim())
    }

    @Test
    fun testGetNext() {
        val connection = URL("${apiUrl}/next").openConnection() as HttpURLConnection
        connection.connect()
        assertEquals(HttpStatus.OK_200, connection.responseCode)
        assertEquals("""{
  "pluginName": "MockPlugin",
  "className": "QueItemMock",
  "name": "3",
  "executeAfterPrevious": false,
  "data": {}
}""".trimIndent(), connection.body().trim())
    }

    @Test
    fun testGetIndex() {
        val connection = URL("${apiUrl}/1").openConnection() as HttpURLConnection
        connection.connect()
        assertEquals(HttpStatus.OK_200, connection.responseCode)
        assertEquals("""{
  "pluginName": "MockPlugin",
  "className": "QueItemMock",
  "name": "2",
  "executeAfterPrevious": false,
  "data": {}
}""".trimIndent(), connection.body().trim())
    }

    @Test
    fun testGetIndexNotFound() {
        val connection = URL("${apiUrl}/10").openConnection() as HttpURLConnection
        connection.connect()
        assertEquals(HttpStatus.OK_200, connection.responseCode)
        assertEquals("null", connection.body().trim())
    }

    @Test
    fun testGetInvalidGetEndpoint() {
        val connection = URL("${apiUrl}/x").openConnection() as HttpURLConnection
        connection.connect()

        assertEquals(HttpStatus.NOT_FOUND_404, connection.responseCode)
        assertEquals("Not Found", connection.errorBody().trim())
    }

    @Test
    fun testGetInvalidPostEndpoint() {
        val connection = URL("${apiUrl}/x").openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.connect()

        assertEquals(HttpStatus.NOT_FOUND_404, connection.responseCode)
        assertEquals("Not Found", connection.errorBody().trim())
    }

    @Test
    fun testPostCurrent() {
        val queueItem = Que.current() as QueItemMock
        assertFalse(queueItem.isActivated)

        val connection = URL("${apiUrl}/current").openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.connect()

        assertEquals(HttpStatus.OK_200, connection.responseCode)
        assertEquals("""{
  "pluginName": "MockPlugin",
  "className": "QueItemMock",
  "name": "2",
  "executeAfterPrevious": false,
  "data": {}
}""".trimIndent(), connection.body().trim())
        assertTrue(queueItem.isActivated)
    }

    @Test
    fun testPostNext() {
        val queueItemCurrent = Que.current() as QueItemMock
        val queueItemNext = Que.previewNext() as QueItemMock
        assertFalse(queueItemCurrent.isActivated)
        assertFalse(queueItemCurrent.isDeactivated)
        assertFalse(queueItemNext.isActivated)

        val connection = URL("${apiUrl}/next").openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.connect()

        assertEquals(HttpStatus.OK_200, connection.responseCode)
        assertEquals("""{
  "pluginName": "MockPlugin",
  "className": "QueItemMock",
  "name": "3",
  "executeAfterPrevious": false,
  "data": {}
}""".trimIndent(), connection.body().trim())
        assertFalse(queueItemCurrent.isActivated)
        assertTrue(queueItemCurrent.isDeactivated)
        assertTrue(queueItemNext.isActivated)
        assertEquals(queueItemNext, Que.current())
    }

    @Test
    fun testPostIndex() {
        assertNotEquals(2, Que.currentIndex())
        val connection = URL("${apiUrl}/2").openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.connect()

        assertEquals(HttpStatus.OK_200, connection.responseCode)
        assertEquals("""{
  "pluginName": "MockPlugin",
  "className": "QueItemMock",
  "name": "3",
  "executeAfterPrevious": false,
  "data": {}
}""".trimIndent(), connection.body().trim())
        assertEquals(2, Que.currentIndex())
    }
}