package api


import config.Config
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletContextHandler
import java.net.URI
import java.util.logging.Logger

object ApiServer {
    private val logger = Logger.getLogger(ApiServer::class.java.name)

    private val server: Server = Server(Config.httpApiServerPort)

    init {
        val apiServletContextHandler = ServletContextHandler()
        apiServletContextHandler.contextPath = "/api/v1"

        logger.fine("Registering API endpoints")
        apiServletContextHandler.addServlet(QueueApiServlet::class.java, "/queue/*")
        apiServletContextHandler.addServlet(QuickAccessButtonsApiServlet::class.java, "/quickAccessButtons/*")
        apiServletContextHandler.addServlet(ConfigApiServlet::class.java, "/config/*")
        apiServletContextHandler.addServlet(NotificationApiServlet::class.java, "/notifications/*")
        server.handler = apiServletContextHandler
    }

    fun start() {
        logger.info("Starting API server...")
        server.start()
        logger.info("API server started on: ${uri()}")
    }

    fun stop() {
        logger.info("Stopping API server...")
        server.stop()
        logger.info("API server stopped")
    }

    fun uri(): URI = server.uri
}