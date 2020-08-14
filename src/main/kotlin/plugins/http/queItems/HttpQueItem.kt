package plugins.http.queItems

import api.body
import api.errorBody
import objects.que.JsonQueue
import objects.que.QueItem
import plugins.http.HttpPlugin
import java.awt.Color
import java.io.IOException
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.util.logging.Logger

class HttpQueItem(
    override val plugin: HttpPlugin,
    override val name: String,
    private val url: String,
    private val method: String,
    private val body: String?
) : QueItem {

    private val logger = Logger.getLogger(HttpQueItem::class.java.name)

    override var executeAfterPrevious = false
    override var quickAccessColor: Color? = plugin.quickAccessColor

    companion object {
        fun fromJson(plugin: HttpPlugin, jsonQueueItem: JsonQueue.QueueItem) : QueItem {
            val url = jsonQueueItem.data["url"]!!
            val method = jsonQueueItem.data["method"]!!
            val body = jsonQueueItem.data["body"]
            return HttpQueItem(plugin, jsonQueueItem.name, url, method, body)
        }
    }

    override fun toJson(): JsonQueue.QueueItem {
        val jsonQueueItem = super.toJson()
        jsonQueueItem.data["url"] = url
        jsonQueueItem.data["method"] = method
        jsonQueueItem.data["body"] = body
        return jsonQueueItem
    }

    override fun activate() {
        logger.info("Performing $method request to $url with body?: $body")
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.requestMethod = method

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
        logger.info("Connection response error: ${connection.errorBody()}")
        try {
            logger.info("Connection response body: ${connection.body()}")
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}