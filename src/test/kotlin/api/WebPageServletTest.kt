package api

import config.Config
import org.eclipse.jetty.http.HttpStatus
import org.junit.AfterClass
import org.junit.BeforeClass
import java.net.ServerSocket
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class WebPageServletTest {
    companion object {
        private var apiRootEndpoint: String = ""
        private var apiUrl: String = "" + apiRootEndpoint

        @BeforeClass
        @JvmStatic
        fun setup() {
            // Get some random free port
            Config.httpApiServerPort = ServerSocket(0).use { it.localPort }

            ApiServer.start()
            apiUrl = ApiServer.url().toString() + apiRootEndpoint
        }

        @AfterClass
        @JvmStatic
        fun tearDown() {
            ApiServer.stop()
        }
    }

    @Test
    fun testGetControlPage() {
        val connection = get("${apiUrl}/control")

        assertEquals(HttpStatus.OK_200, connection.responseCode)

        val body = connection.body()
        assertTrue(body.contains("<html>"));
        assertTrue(body.contains("previous"));
    }


}