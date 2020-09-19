package plugins.tallyLight


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
        } catch (t: Throwable) {
            logger.warning("Failed to send update to tally: $cameraSourceName")
            t.printStackTrace()
        }
    }

    override fun toString(): String {
        return "TallyLight(cameraSourceName=$cameraSourceName, host=$host, isLive=$isLive)"
    }
}