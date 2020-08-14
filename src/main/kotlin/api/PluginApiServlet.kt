package api


import plugins.PluginLoader
import java.util.logging.Logger
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

data class JsonPlugin(
    val name: String,
    val description: String,
    val version: String
)

@Suppress("UNUSED_PARAMETER")
class PluginApiServlet : HttpServlet() {
    private val logger = Logger.getLogger(ConfigApiServlet::class.java.name)

    operator fun Regex.contains(text: CharSequence?): Boolean = this.matches(text ?: "")

    override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        logger.info("Processing ${request.method} request from : ${request.requestURI}")

        when (request.pathInfo) {
            "/list" -> getList(request, response)
            else -> respondWithNotFound(response)
        }
    }

    private fun getList(request: HttpServletRequest, response: HttpServletResponse) {
        logger.info("Getting Plugins list")

        val jsonObjects = PluginLoader.allPlugins.map { JsonPlugin(it.name, it.description, it.version) }

        respondWithJson(response, jsonObjects)
    }
}