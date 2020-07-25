package api


import com.google.gson.GsonBuilder
import config.Config
import java.util.logging.Logger
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

data class JsonConfigPair(val key: String, val value: Any?)

class ConfigApiServlet : HttpServlet() {
    private val logger = Logger.getLogger(ConfigApiServlet::class.java.name)

    operator fun Regex.contains(text: CharSequence?): Boolean = this.matches(text ?: "")
    private val configKeyMatcher = """^/(\w+)$""".toRegex()

    override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        logger.info("Processing ${request.method} request from : ${request.requestURI}")

        when (request.pathInfo) {
            "/list" -> getList(response)
            in Regex(configKeyMatcher.pattern) -> getKeyValue(response, request.pathInfo.getParams(configKeyMatcher))
            else -> respondWithNotFound(response)
        }
    }

    override fun doPost(request: HttpServletRequest, response: HttpServletResponse) {
        logger.info("Processing ${request.method} request from : ${request.requestURI}")

        when (request.pathInfo) {
            else -> respondWithNotFound(response)
        }
    }

    private fun getList(response: HttpServletResponse) {
        val pairs = Config.fields().map {
            JsonConfigPair(
                key = it.name,
                value = it.get(Config)
            )
        }
        val json = GsonBuilder().setPrettyPrinting().create().toJson(pairs)

        respondWithJson(response, json)
    }

    private fun getKeyValue(response: HttpServletResponse, params: List<String>) {
        val key = params[0]
        logger.info("Getting Config value for key: $key")

        val value = Config.get(key)

        val jsonObject = JsonConfigPair(key, value)
        val json = GsonBuilder().setPrettyPrinting().create().toJson(jsonObject)

        respondWithJson(response, json)
    }

//    private fun postKeyValue(request: HttpServletRequest, response: HttpServletResponse, params: List<String>) {
//        val key = params[0]
//        logger.info("Setting new Config value for key: $key")
//
//        val json = request.inputStream.bufferedReader().readText()
//        val pair = Gson().fromJson(json, JsonConfigPair::class.java)
//        logger.info(pair.toString())
//
//        if (key != pair.key) {
//            logger.info("Request parameter key doesn't match request body key")
//            respondWithJson(response, "null")
//            return
//        }
//
//        logger.info("New value for key is: ${pair.value}")
//        Config.set(pair.key, pair.value)
//
//        getKeyValue(response, params)
//    }
}