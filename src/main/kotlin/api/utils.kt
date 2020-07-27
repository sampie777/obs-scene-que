package api

import java.io.InputStream
import java.net.HttpURLConnection
import java.util.logging.Logger
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

private val logger: Logger = Logger.getLogger("utils")

fun respondWithJson(
    response: HttpServletResponse,
    json: String,
    status: Int = HttpServletResponse.SC_OK
) {
    response.status = status
    response.contentType = "application/json"
    response.writer.println(json)
    logger.info("Response: $json")
}

fun respondWithNotFound(response: HttpServletResponse) {
    response.status = HttpServletResponse.SC_NOT_FOUND
    response.writer.println("Not Found")
}

fun String.getPathVariables(regex: Regex): List<String> = regex.find(this)?.destructured?.toList() ?: emptyList()

fun HttpServletRequest.getQueryParameter(key: String, default: Any?): Any? {
    val param = this.parameterMap[key] ?: return default
    return param[0] ?: default
}

fun HttpURLConnection.body() = (this.content as InputStream).bufferedReader().readText()
fun HttpURLConnection.errorBody() = this.errorStream.bufferedReader().readText()
fun HttpServletRequest.body() = this.inputStream.bufferedReader().readText()
