package plugins.tallyLight


import api.ApiServer
import java.net.URLEncoder
import java.util.logging.Logger

class TallyLight(
    var cameraSourceName: String,
    var host: String = "",
    var isLive: Boolean = false
) {
    private val logger = Logger.getLogger(TallyLight::class.java.name)

    fun update() {
        Thread {
            sendUpdateBlocking()
        }.start()
    }

    private fun sendUpdateBlocking() {
        logger.info("Sending Tally Light update for light: $cameraSourceName")

        try {
            val encodedSourceName = URLEncoder.encode(cameraSourceName, "utf-8")
            post("${ApiServer.url()}/api/v1/tallylight/set/$encodedSourceName", "$isLive")
        } catch (t: Throwable) {
            logger.warning("Failed to send update to internal tally API: $cameraSourceName")
            t.printStackTrace()
        }

        if (host.isEmpty()) {
            return
        }

        try {
            get("http://${host}/" + if (isLive) "on" else "off")
        } catch (t: Throwable) {
            logger.warning("Failed to send update to tally: $cameraSourceName")
            t.printStackTrace()
        }
    }
}