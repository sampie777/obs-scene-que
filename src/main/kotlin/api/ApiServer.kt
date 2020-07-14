package api


import config.Config
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletContextHandler
import java.util.logging.Logger

object ApiServer {
    private val logger = Logger.getLogger(ApiServer::class.java.name)

    private var server: Server = Server(Config.httpApiServerPort)

    init {
        val apiServletContextHandler = ServletContextHandler()
        apiServletContextHandler.contextPath = "/api/v1"
        apiServletContextHandler.addServlet(QueueApiServlet::class.java, "/queue/*")
        apiServletContextHandler.addServlet(QuickAccessButtonsApiServlet::class.java, "/quickAccessButtons/*")
        server.handler = apiServletContextHandler
    }

    fun start() {
        logger.info("Starting API server...")
        server.start()
        logger.info("API server started")
    }

    fun stop() {
        logger.info("Stopping API server...")
        server.stop()
        logger.info("API server stopped")
    }
}