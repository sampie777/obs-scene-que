package api

import org.eclipse.jetty.http.HttpMethod
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

fun get(url: String): HttpURLConnection {
    val connection = URL(url).openConnection() as HttpURLConnection
    connection.requestMethod = HttpMethod.GET.toString()
    connection.connect()
    return connection
}

fun post(url: String, body: String? = null): HttpURLConnection {
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
    return connection
}

fun delete(url: String): HttpURLConnection {
    val connection = URL(url).openConnection() as HttpURLConnection
    connection.requestMethod = HttpMethod.DELETE.toString()
    connection.connect()
    return connection
}
