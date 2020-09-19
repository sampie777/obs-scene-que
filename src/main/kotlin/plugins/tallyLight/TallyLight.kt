package plugins.tallyLight


import java.net.NoRouteToHostException
import java.util.logging.Logger

class TallyLight(
    var cameraSourceName: String,
    var host: String = "",
    var isLive: Boolean = false
) {
    private val logger = Logger.getLogger(TallyLight::class.java.name)

    fun update(async: Boolean = true) {
        val thread = Thread { sendUpdateBlocking() }
        thread.start()

        if (!async) {
            thread.join(TallyLightProperties.lightConnectionTimeout)
        }
    }

    private fun sendUpdateBlocking() {
        logger.info("Sending Tally Light update for light: $cameraSourceName")

        if (host.isEmpty()) {
            return
        }

        try {
            get("http://${host}/" + if (isLive) "on" else "off")
        } catch (e: NoRouteToHostException) {
            logger.warning("Could not connect to tally light: $this")
        } catch (t: Throwable) {
            logger.warning("Failed to send update to tally light: $this")
            t.printStackTrace()
        }
    }

    override fun toString(): String {
        return "TallyLight(cameraSourceName=$cameraSourceName, host=$host, isLive=$isLive)"
    }
}