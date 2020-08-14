package api

import config.Config
import gui.quickAccessButtons.QuickAccessButton
import mocks.MockPlugin
import mocks.QueItemMock
import objects.State
import org.eclipse.jetty.http.HttpStatus
import org.junit.AfterClass
import org.junit.BeforeClass
import java.net.HttpURLConnection
import java.net.ServerSocket
import java.net.URL
import kotlin.test.*


@Suppress("DEPRECATION")
class QuickAccessButtonsApiServletTest {
    companion object {
        private var apiRootEndpoint: String = "/api/v1/quickAccessButtons"
        private var apiUrl: String = "" + apiRootEndpoint

        @BeforeClass
        @JvmStatic
        fun setup() {
            // Get some random free port
            Config.httpApiServerPort = ServerSocket(0).use { it.localPort }

            ApiServer.start()
            apiUrl = ApiServer.url() + apiRootEndpoint
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
        State.quickAccessButtons.clear()

        State.quickAccessButtons.add(QuickAccessButton(0, mockPlugin.configStringToQueItem("1")))
        State.quickAccessButtons.add(QuickAccessButton(1, null))
        State.quickAccessButtons.add(QuickAccessButton(2, mockPlugin.configStringToQueItem("2")))
    }

    @Test
    fun testGetEmptyList() {
        State.quickAccessButtons.clear()

        val connection = URL("${apiUrl}/list").openConnection() as HttpURLConnection
        connection.connect()
        assertEquals(HttpStatus.OK_200, connection.responseCode)
        assertEquals("""{
  "data": []
}""".trimIndent(), connection.body().trim())
    }

    @Test
    fun testGetListWithOneNull() {
        State.quickAccessButtons.clear()
        State.quickAccessButtons.add(QuickAccessButton(0, null))

        val connection = URL("${apiUrl}/list").openConnection() as HttpURLConnection
        connection.connect()
        assertEquals(HttpStatus.OK_200, connection.responseCode)
        assertEquals("""{
  "data": [
    null
  ]
}""".trimIndent(), connection.body().trim())
    }

    @Test
    fun testGetList() {
        val connection = URL("${apiUrl}/list").openConnection() as HttpURLConnection
        connection.connect()
        assertEquals(HttpStatus.OK_200, connection.responseCode)
        assertEquals("""{
  "data": [
    {
      "pluginName": "MockPlugin",
      "className": "QueItemMock",
      "name": "1",
      "executeAfterPrevious": false,
      "quickAccessColor": null,
      "data": {}
    },
    null,
    {
      "pluginName": "MockPlugin",
      "className": "QueItemMock",
      "name": "2",
      "executeAfterPrevious": false,
      "quickAccessColor": null,
      "data": {}
    }
  ]
}""".trimIndent(), connection.body().trim())
    }

    @Test
    fun testGetIndex() {
        val connection = URL("${apiUrl}/0").openConnection() as HttpURLConnection
        connection.connect()
        assertEquals(HttpStatus.OK_200, connection.responseCode)
        assertEquals("""{
  "data": {
    "pluginName": "MockPlugin",
    "className": "QueItemMock",
    "name": "1",
    "executeAfterPrevious": false,
    "quickAccessColor": null,
    "data": {}
  }
}""".trimIndent(), connection.body().trim())
    }

    @Test
    fun testGetIndex2() {
        val connection = get("${apiUrl}/1")
        assertEquals(HttpStatus.OK_200, connection.responseCode)
        assertEquals("""{
  "data": null
}""".trimMargin(), connection.body().trim())
    }

    @Test
    fun testGetIndexNotFound() {
        val connection = get("${apiUrl}/10")

        assertEquals(HttpStatus.OK_200, connection.responseCode)
        assertEquals("""{
  "data": null
}""".trimMargin(), connection.body().trim())
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
    fun testPostIndex() {
        val queueItem = State.quickAccessButtons[2].getQueItem() as QueItemMock
        assertFalse(queueItem.isActivated)

        val connection = post("${apiUrl}/2")

        assertEquals(HttpStatus.OK_200, connection.responseCode)
        assertEquals("""{
  "data": {
    "pluginName": "MockPlugin",
    "className": "QueItemMock",
    "name": "2",
    "executeAfterPrevious": false,
    "quickAccessColor": null,
    "data": {}
  }
}""".trimIndent(), connection.body().trim())
        assertTrue(queueItem.isActivated)
    }
}