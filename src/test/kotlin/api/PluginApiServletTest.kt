package api

import com.google.gson.Gson
import com.google.gson.JsonObject
import config.Config
import mocks.MockPlugin
import org.eclipse.jetty.http.HttpStatus
import org.junit.AfterClass
import org.junit.BeforeClass
import plugins.PluginLoader
import java.net.ServerSocket
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class PluginApiServletTest {
    companion object {
        private var apiRootEndpoint: String = "/plugins"
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
        PluginLoader.allPlugins.clear()
        PluginLoader.queItemPlugins.clear()
        PluginLoader.loadInternalPlugins()

        PluginLoader.enableAll()
        assertEquals(2, PluginLoader.allPlugins.size)
        assertEquals(2, PluginLoader.queItemPlugins.size)
    }

    @Test
    fun testGetList() {
        val connection = get("${apiUrl}/list")

        assertEquals(HttpStatus.OK_200, connection.responseCode)

        val body = connection.body()
        val obj = Gson().fromJson(body, JsonObject::class.java)
        val array = obj.get("data").asJsonArray
        assertEquals(2, array.size())
        assertTrue(array[0].asJsonObject.get("name").asString in listOf("ObsPlugin", "TextPlugin"))
        assertTrue(array[1].asJsonObject.get("name").asString in listOf("ObsPlugin", "TextPlugin"))
    }

    @Test
    fun testGetListWithOnePlugin() {
        PluginLoader.allPlugins.clear()
        val mockPlugin = MockPlugin()
        PluginLoader.allPlugins.add(mockPlugin)

        val connection = get("${apiUrl}/list")

        assertEquals(HttpStatus.OK_200, connection.responseCode)

        val body = connection.body().trim()
        assertEquals("""{
  "data": [
    {
      "name": "MockPlugin",
      "description": "description",
      "version": "0.0.0"
    }
  ]
}""", body)
    }
}