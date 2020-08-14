package api

import config.Config
import org.eclipse.jetty.http.HttpStatus
import org.junit.AfterClass
import org.junit.BeforeClass
import java.net.ServerSocket
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class ConfigApiServletTest {
    companion object {
        private var apiRootEndpoint: String = "/config"
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
        Config.obsReconnectionTimeout = 3001
    }

    @Test
    fun testGetConfigKeyValue() {
        val connection = get("${apiUrl}/obsReconnectionTimeout")

        assertEquals(HttpStatus.OK_200, connection.responseCode)
        assertEquals("""{
  "data": {
    "key": "obsReconnectionTimeout",
    "value": 3001
  }
}""".trimIndent(), connection.body().trim())
    }

    @Test
    fun testGetInvalidConfigKeyValue() {
        val connection = get("${apiUrl}/xx")

        assertEquals(HttpStatus.OK_200, connection.responseCode)
        assertEquals("""{
  "data": {
    "key": "xx",
    "value": null
  }
}""".trimIndent(), connection.body().trim())
    }

    @Test
    fun testGetList() {
        val connection = get("${apiUrl}/list")

        assertEquals(HttpStatus.OK_200, connection.responseCode)
        val body = connection.body().trim()
        assertTrue(body.startsWith("""{
  "data": ["""))
        assertTrue(body.endsWith("""]
}"""))
        assertTrue(body.contains("""{
      "key": "obsReconnectionTimeout",
      "value": 3001
    }""".trimIndent()))
    }

    @Test
    fun testGetInvalidPostEndpoint() {
        val connection = post("${apiUrl}/x")

        assertEquals(HttpStatus.NOT_FOUND_404, connection.responseCode)
        assertEquals("Not Found", connection.errorBody()?.trim())
    }
}