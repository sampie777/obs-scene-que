package api


import plugins.tallyLight.TallyLightPlugin
import java.net.URLDecoder
import java.util.logging.Logger
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Suppress("UNUSED_PARAMETER")
class TallyLightApiServlet : HttpServlet() {
    private val logger = Logger.getLogger(TallyLightApiServlet::class.java.name)

    private val tallies: HashMap<String, Boolean> = TallyLightPlugin.tallies.map {
        it.cameraSourceName to false
    }.toMap() as HashMap<String, Boolean>

    operator fun Regex.contains(text: CharSequence?): Boolean = this.matches(text ?: "")
    private val nameMatcher = """^/set/(.*?)$""".toRegex()

    override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        logger.info("Processing ${request.method} request from : ${request.requestURI}")

        when (request.pathInfo) {
            "/preview" -> getIndex(request, response)
            else -> respondWithNotFound(response)
        }
    }

    override fun doPost(request: HttpServletRequest, response: HttpServletResponse) {
        logger.info("Processing ${request.method} request from : ${request.requestURI}")

        when (request.pathInfo) {
            in Regex(nameMatcher.pattern) -> postSet(request, response, request.pathInfo.getPathVariables(nameMatcher))
            else -> respondWithNotFound(response)
        }
    }

    private fun getIndex(request: HttpServletRequest, response: HttpServletResponse) {
        val tallieDivs = tallies.keys.joinToString("") {
            "<div class=\"live-${tallies[it]}\">${it}</div>"
        }
        respondWithHtml(
            response, """
            <html>
<head>
 <meta http-equiv="refresh" content="1">
    <style>
        body {
            background-color: #555;
            font-size: 32pt;
            color: #333;
        }
        div {
            width: 200px;
            height: 200px;
            margin: 10px;
            display: inline-block;
            background-color: #aaa;
        }
        .live-true {
            background-color: red;
        }
    </style>
</head>
<body>
$tallieDivs
</body>
</html>
        """.trimIndent(),
            log = false
        )
    }

    private fun postSet(request: HttpServletRequest, response: HttpServletResponse, params: List<String>) {
        val decodedTally = URLDecoder.decode(params[0], "utf-8")
        if (decodedTally !in tallies.keys) {
            logger.warning("Tally not found: ${params[0]} / $decodedTally")
            respondWithNotFound(response)
            return
        }

        val body = request.body()
        logger.info("Received request body: $body")

        tallies[decodedTally] = (body == "true")

        respondWithJson(
            response, mapOf(
                "result" to "ok",
                "tallyLight" to decodedTally,
                "isLive" to tallies[decodedTally]
            )
        )
    }
}