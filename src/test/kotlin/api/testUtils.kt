package api

import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

fun get(url: String): HttpURLConnection {
    val connection = URL(url).openConnection() as HttpURLConnection
    connection.connect()
    return connection
}

fun post(url: String, body: String? = null): HttpURLConnection {
    val connection = URL(url).openConnection() as HttpURLConnection
    connection.requestMethod = "POST"

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
    return connection
}
