package api


import GUI
import config.Config
import objects.que.Que
import objects.que.QueLoader
import java.util.logging.Logger
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Suppress("UNUSED_PARAMETER")
class QueueApiServlet : HttpServlet() {
    private val logger = Logger.getLogger(QueueApiServlet::class.java.name)

    operator fun Regex.contains(text: CharSequence?): Boolean = this.matches(text ?: "")
    private val indexMatcher = """^/(\d+)$""".toRegex()
    private val addQueueItemMatcher = """^/add(/(\d+))?$""".toRegex()

    override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        logger.info("Processing ${request.method} request from : ${request.requestURI}")

        when (request.pathInfo) {
            "/list" -> getList(response)
            "/current" -> getCurrent(response)
            "/previous" -> getPrevious(response)
            "/next" -> getNext(response)
            in Regex(indexMatcher.pattern) -> getIndex(response, request.pathInfo.getPathVariables(indexMatcher))
            else -> respondWithNotFound(response)
        }
    }

    override fun doPost(request: HttpServletRequest, response: HttpServletResponse) {
        logger.info("Processing ${request.method} request from : ${request.requestURI}")

        when (request.pathInfo) {
            "/current" -> postCurrent(response)
            "/previous" -> postPrevious(response)
            "/next" -> postNext(response)
            in Regex(addQueueItemMatcher.pattern) -> postAdd(request, response, request.pathInfo.getPathVariables(addQueueItemMatcher))
            in Regex(indexMatcher.pattern) -> postIndex(request, response, request.pathInfo.getPathVariables(indexMatcher))
            else -> respondWithNotFound(response)
        }
    }

    override fun doDelete(request: HttpServletRequest, response: HttpServletResponse) {
        logger.info("Processing ${request.method} request from : ${request.requestURI}")

        when (request.pathInfo) {
            in Regex(indexMatcher.pattern) -> deleteIndex(request, response, request.pathInfo.getPathVariables(indexMatcher))
            else -> respondWithNotFound(response)
        }
    }

    private fun getList(response: HttpServletResponse) {
        logger.info("Getting Queue list")
        val json = QueLoader.queueToJsonObject()

        respondWithJson(response, json)
    }

    private fun getCurrent(response: HttpServletResponse) {
        logger.info("Getting current Queue item")
        val queueItemJson = Que.current()?.toJson()

        respondWithJson(response, queueItemJson)
    }

    private fun getPrevious(response: HttpServletResponse) {
        logger.info("Getting previous Queue item")
        val queueItemJson = Que.previewPrevious()?.toJson()

        respondWithJson(response, queueItemJson)
    }

    private fun getNext(response: HttpServletResponse) {
        logger.info("Getting next Queue item")
        val queueItemJson = Que.previewNext()?.toJson()

        respondWithJson(response, queueItemJson)
    }

    private fun getIndex(response: HttpServletResponse, params: List<String>) {
        val index = params[0].toInt()
        logger.info("Getting Queue index: $index")

        val queueItemJson = Que.getAt(index)?.toJson()

        respondWithJson(response, queueItemJson)
    }

    private fun postCurrent(response: HttpServletResponse) {
        logger.info("Activating current Queue item")
        Que.activateCurrent(executeExecuteAfterPrevious = false)
        getCurrent(response)
    }

    private fun postPrevious(response: HttpServletResponse) {
        logger.info("Activating previous Queue item")
        Que.previous()
        getCurrent(response)
    }

    private fun postNext(response: HttpServletResponse) {
        logger.info("Activating next Queue item")
        Que.next()
        getCurrent(response)
    }

    private fun postIndex(request: HttpServletRequest, response: HttpServletResponse, params: List<String>) {
        val index = params[0].toInt()
        logger.info("Activating Queue index: $index")

        val queueItem = Que.getAt(index)
        if (queueItem == null) {
            respondWithNotFound(response)
            return
        }

        Que.deactivateCurrent()
        Que.setCurrentQueItemByIndex(index)

        val activateNextSubQueueItems =
            when {
                request.getParameter("activateNextSubQueueItems") != null -> request.getParameter("activateNextSubQueueItems") == "true"
                Que.current() != null && Que.current()!!.executeAfterPrevious -> Config.activateNextSubQueueItemsOnMouseActivationSubQueueItem
                else -> Config.activateNextSubQueueItemsOnMouseActivationQueueItem
            }

        Que.activateCurrent(activateNextSubQueueItems)

        getCurrent(response)
    }

    private fun postAdd(request: HttpServletRequest, response: HttpServletResponse, params: List<String>) {
        val index: Int? = if (params.size == 2 && params[1].isNotBlank()) {
            logger.info("Adding new Queue item at index: ${params[1]}")
            params[1].toInt()
        } else {
            logger.info("Adding new Queue item")
            null
        }

        val json = request.body()
        val jsonQueueItem = QueLoader.jsonQueItemFromJson(json)

        if (jsonQueueItem == null) {
            logger.warning("Failed to convert json to jsonQueueItem. Json: $json")
            respondWithJson(response, null)
            return
        }

        val queueItem = QueLoader.loadQueItemFromJson(jsonQueueItem)

        if (queueItem == null) {
            logger.warning("Failed to create Queue Item")
            respondWithJson(response, null)
            return
        }

        logger.info(queueItem.toString())

        if (index == null) {
            Que.add(queueItem)
        } else {
            Que.add(index, queueItem)
        }

        GUI.refreshQueItems()

        respondWithJson(response, queueItem.toJson())
    }

    private fun deleteIndex(request: HttpServletRequest, response: HttpServletResponse, params: List<String>) {
        val index = params[0].toInt()
        logger.info("Removing Queue index: $index")

        val queueItem = Que.remove(index)
        if (queueItem == null) {
            respondWithNotFound(response)
            return
        }

        GUI.refreshQueItems()

        respondWithJson(response, queueItem.toJson())
    }
}