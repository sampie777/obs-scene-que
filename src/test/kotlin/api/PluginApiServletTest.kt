package api

import com.google.gson.Gson
import com.google.gson.JsonArray
import config.Config
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
        val obj = Gson().fromJson(body, JsonArray::class.java)
        assertEquals(2, obj.size())
        assertTrue(obj[0].asJsonObject.get("name").asString in listOf("ObsPlugin", "TextPlugin"))
        assertTrue(obj[1].asJsonObject.get("name").asString in listOf("ObsPlugin", "TextPlugin"))
    }
}