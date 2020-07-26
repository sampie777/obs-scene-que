package api


import jsonBuilder
import objects.State
import java.util.logging.Logger
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class QuickAccessButtonsApiServlet : HttpServlet() {
    private val logger = Logger.getLogger(QuickAccessButtonsApiServlet::class.java.name)

    operator fun Regex.contains(text: CharSequence?): Boolean = this.matches(text ?: "")
    private val indexMatcher = """^/(\d+)$""".toRegex()

    override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        logger.info("Processing ${request.method} request from : ${request.requestURI}")

        when (request.pathInfo) {
            "/list" -> getList(response)
            in Regex(indexMatcher.pattern) -> getIndex(response, request.pathInfo.getPathVariables(indexMatcher))
            else -> respondWithNotFound(response)
        }
    }
    override fun doPost(request: HttpServletRequest, response: HttpServletResponse) {
        logger.info("Processing ${request.method} request from : ${request.requestURI}")

        when (request.pathInfo) {
            in Regex(indexMatcher.pattern) -> postIndex(response, request.pathInfo.getPathVariables(indexMatcher))
            else -> respondWithNotFound(response)
        }
    }

    private fun getList(response: HttpServletResponse) {
        logger.info("Getting QuickAccessButtons list")
        val queueItems = State.quickAccessButtons.map { it.getQueItem()?.toJson() }
        val json = jsonBuilder().toJson(queueItems)

        respondWithJson(response, json)
    }

    private fun getIndex(response: HttpServletResponse, params: List<String>) {
        val index = params[0].toInt()
        logger.info("Getting Quick Access Button index: $index")

        val button = State.quickAccessButtons.getOrNull(index)
        if (button?.getQueItem() == null) {
            respondWithJson(response, "null")
            return
        }

        val queueItem = button.getQueItem()!!.toJson()
        val json = jsonBuilder().toJson(queueItem)

        respondWithJson(response, json)
    }

    private fun postIndex(response: HttpServletResponse, params: List<String>) {
        val index = params[0].toInt()
        logger.info("Getting Quick Access Button index: $index")

        if (index >= State.quickAccessButtons.size) {
            logger.info("Quick Access Button list out of bounds")
            respondWithJson(response, "null")
            return
        }

        val button = State.quickAccessButtons[index]
        button.doClick()

        getIndex(response, listOf(index.toString()))
    }
}