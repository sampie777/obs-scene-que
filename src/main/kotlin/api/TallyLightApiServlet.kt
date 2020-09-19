package api


import plugins.tallyLight.TallyLightPlugin
import java.util.logging.Logger
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Suppress("UNUSED_PARAMETER")
class TallyLightApiServlet : HttpServlet() {
    private val logger = Logger.getLogger(TallyLightApiServlet::class.java.name)

    operator fun Regex.contains(text: CharSequence?): Boolean = this.matches(text ?: "")

    override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        logger.info("Processing ${request.method} request from : ${request.requestURI}")

        when (request.pathInfo) {
            "/preview" -> getIndex(request, response)
            "/list" -> getList(request, response)
            else -> respondWithNotFound(response)
        }
    }

    override fun doPost(request: HttpServletRequest, response: HttpServletResponse) {
        logger.info("Processing ${request.method} request from : ${request.requestURI}")

        when (request.pathInfo) {
            else -> respondWithNotFound(response)
        }
    }

    private fun getIndex(request: HttpServletRequest, response: HttpServletResponse) {
        val tallieDivs = TallyLightPlugin.tallies.joinToString("") {
            "<div class=\"live-${it.isLive}\">${it.cameraSourceName}</div>"
        }
        respondWithHtml(
            response, """
            <html>
<head>
    <style>
        body {
            background-color: #555;
            font-size: 32pt;
            color: #333;
        }

        #status div {
            width: 200px;
            height: 200px;
            margin: 10px;
            display: inline-block;
            background-color: #aaa;
        }

        #status .live-true {
            background-color: red;
        }
    </style>
</head>
<body>
<div id="status"></div>

<script>
    const apiBaseUrl = "/api/v1";
    const apiStatusEndpoint = "/tallylight/list";
    const statusDiv = document.getElementById("status");

    const get = (url) => fetch(url, {
        method: "GET"
    });

    function updateWithStatus(status) {
        // Clear div
        statusDiv.innerHTML = "";

        status.forEach(tally => {
            const tallyDiv = document.createElement("div");
            tallyDiv.classList.add("live-" + tally.isLive);
            tallyDiv.innerText = tally.cameraSourceName;
            statusDiv.appendChild(tallyDiv);
        })
    }

    function getStatus() {
        get(apiBaseUrl + apiStatusEndpoint)
            .then(response => response.json())
            .then(data => {
                updateWithStatus(data.data);
            })
            .catch(error => console.error('Error getting status', error));
    }

    getStatus();
    const updateInterval = window.setInterval(getStatus, 1000);

    function stopUpdates() {
        window.clearInterval(updateInterval);
    }
</script>
</body>
</html>
        """.trimIndent(),
            log = false
        )
    }

    private fun getList(request: HttpServletRequest, response: HttpServletResponse) {
        val list = TallyLightPlugin.tallies.map { light ->
            mapOf(
                "cameraSourceName" to light.cameraSourceName,
                "host" to light.host,
                "isLive" to light.isLive
            )
        }
        respondWithJson(response, list, log = false)
    }
}