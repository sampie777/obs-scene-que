package api

import java.util.logging.Logger
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

fun String.getParams(regex: Regex): List<String> = regex.find(this)?.destructured?.toList() ?: emptyList()