package api


import com.google.gson.Gson
import objects.OBSClient
import objects.OBSState
import plugins.tallyLight.TallyLight
import plugins.tallyLight.TallyLightPlugin
import plugins.tallyLight.json.TallyLightJson
import java.util.logging.Logger
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Suppress("UNUSED_PARAMETER", "UNCHECKED_CAST")
class TallyLightApiServlet : HttpServlet() {
    private val logger = Logger.getLogger(TallyLightApiServlet::class.java.name)

    operator fun Regex.contains(text: CharSequence?): Boolean = this.matches(text ?: "")

    override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        logger.info("Processing ${request.method} request from : ${request.requestURI}")

        when (request.pathInfo) {
            "/web/overview" -> getOverviewWebPage(request, response)
            "/web/config" -> getConfigWebPage(request, response)
            "/list" -> getList(request, response)
            "/sources" -> getSources(request, response)
            "/filters" -> getFilters(request, response)
            else -> respondWithNotFound(response)
        }
    }

    override fun doPost(request: HttpServletRequest, response: HttpServletResponse) {
        logger.info("Processing ${request.method} request from : ${request.requestURI}")

        when (request.pathInfo) {
            "/add" -> postAdd(request, response)
            "/addAll" -> postAddAll(request, response)
            "/remove" -> postRemove(request, response)
            "/removeAll" -> postRemoveAll(request, response)
            else -> respondWithNotFound(response)
        }
    }

    private fun getOverviewWebPage(request: HttpServletRequest, response: HttpServletResponse) {
        val html = TallyLightApiServlet::class.java.getResource("/api/tallyLightOverview.html").readText(Charsets.UTF_8)
        respondWithHtml(response, html)
    }

    private fun getConfigWebPage(request: HttpServletRequest, response: HttpServletResponse) {
        val html = TallyLightApiServlet::class.java.getResource("/api/tallyLightConfig.html").readText(Charsets.UTF_8)
        respondWithHtml(response, html)
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

    private fun getSources(request: HttpServletRequest, response: HttpServletResponse) {
        val sources = OBSState.scenes.flatMap { scene ->
            scene.sources.map { source ->
                source.name
            }
        }
            .distinct()
            .sorted()

        respondWithJson(response, sources)
    }

    private fun getFilters(request: HttpServletRequest, response: HttpServletResponse) {
        val list = TallyLightPlugin.filters.map { filter ->
            mapOf(
                "name" to filter.javaClass.name,
                "enabled" to filter.isEnabled
            )
        }

        respondWithJson(response, list)
    }

    private fun postAdd(request: HttpServletRequest, response: HttpServletResponse) {
        logger.info("Add request")
        val body = request.body()

        val tallyLightJson = try {
            Gson().fromJson(body, TallyLightJson::class.java)
        } catch (e: Exception) {
            logger.info("Exception caught while turning post body into json object: $body")
            e.printStackTrace()
            respondWithNotFound(response)
            return
        }

        val tallyLight = try {
            TallyLight(cameraSourceName = tallyLightJson.cameraSourceName, host = tallyLightJson.host)
        } catch (e: Exception) {
            logger.info("Exception caught while turning JSON into Tally Light object: $tallyLightJson")
            e.printStackTrace()
            respondWithNotFound(response)
            return
        }

        if (tallyLight.cameraSourceName.isEmpty()) {
            respondWithJson(response, "cameraSourceName may not be empty")
            return
        }

        logger.info("Adding Tally Light: $tallyLight")
        TallyLightPlugin.tallies.add(tallyLight)

        respondWithJson(response, tallyLightJson)
    }

    private fun postAddAll(request: HttpServletRequest, response: HttpServletResponse) {
        logger.info("AddAll request")
        val body = request.body()

        val listJson = try {
            fromJsonAdvanced(body) as List<TallyLightJson>
        } catch (e: Exception) {
            logger.info("Exception caught while turning post body into json object: $body")
            e.printStackTrace()
            respondWithNotFound(response)
            return
        }

        val tallyLights = try {
            listJson.map {
                if (it.cameraSourceName.isEmpty()) {
                    respondWithJson(response, "cameraSourceName may not be empty for: $it")
                    return
                }

                TallyLight(cameraSourceName = it.cameraSourceName, host = it.host)
            }
        } catch (e: Exception) {
            logger.info("Exception caught while turning JSON into Tally Lights object: $listJson")
            e.printStackTrace()
            respondWithNotFound(response)
            return
        }

        logger.info("Adding all Tally Lights: $tallyLights")
        TallyLightPlugin.removeAllTallies()
        TallyLightPlugin.tallies.addAll(tallyLights)

        respondWithJson(response, listJson)

        // Update lights
        OBSClient.loadScenes()
    }

    private fun postRemove(request: HttpServletRequest, response: HttpServletResponse) {
        logger.info("Remove request")
        val body = request.body()

        val tallyLightJson = try {
            Gson().fromJson(body, TallyLightJson::class.java)
        } catch (e: Exception) {
            logger.info("Exception caught while turning post body into json object: $body")
            e.printStackTrace()
            respondWithNotFound(response)
            return
        }

        val tallyLight = try {
            TallyLight(cameraSourceName = tallyLightJson.cameraSourceName, host = tallyLightJson.host)
        } catch (e: Exception) {
            logger.info("Exception caught while turning JSON into Tally Light object: $tallyLightJson")
            e.printStackTrace()
            respondWithNotFound(response)
            return
        }

        logger.info("Removing Tally Light: $tallyLightJson")
        tallyLight.isLive = false
        tallyLight.update()

        TallyLightPlugin.tallies.removeIf {
            it.cameraSourceName == tallyLight.cameraSourceName
                    && it.host == tallyLight.host
        }

        respondWithJson(response, tallyLightJson)
    }

    private fun postRemoveAll(request: HttpServletRequest, response: HttpServletResponse) {
        logger.info("RemoveAll request")

        logger.info("Removing all Tally Lights")
        TallyLightPlugin.removeAllTallies()

        respondWithJson(response, "ok")
    }
}