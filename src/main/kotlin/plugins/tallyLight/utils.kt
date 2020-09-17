package plugins.tallyLight

import api.body
import api.errorBody
import org.eclipse.jetty.http.HttpMethod
import java.io.IOException
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.util.logging.Logger

private val logger = Logger.getLogger("utils")

fun get(url: String): HttpURLConnection {
    val connection = URL(url).openConnection() as HttpURLConnection
    connection.requestMethod = HttpMethod.GET.toString()
    connection.connect()
    logger.info("Connection response code: ${connection.responseCode}")

    if (connection.errorBody() != null) {
        logger.info("Connection response error: ${connection.errorBody()}")
    }

    try {
        logger.info("Connection response body: ${connection.body()}")
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return connection
}

fun post(url: String, body: String? = null): HttpURLConnection {
    logger.info("Sending post requests to: $url")

    val connection = URL(url).openConnection() as HttpURLConnection
    connection.requestMethod = HttpMethod.POST.toString()

    if (body != null) {
        connection.setRequestProperty("Content-Type", "application/json; utf-8")
        connection.setRequestProperty("Accept", "application/json")
        connection.doOutput = true

        OutputStreamWriter(connection.outputStream).run {
            write(body)
            flush()
        }
    }

    connection.connect()
    logger.info("Connection response code: ${connection.responseCode}")

    if (connection.errorBody() != null) {
        logger.info("Connection response error: ${connection.errorBody()}")
    }

    try {
        logger.info("Connection response body: ${connection.body()}")
    } catch (e: IOException) {
    }
    return connection
}