package plugins.tallyLight.filters


import plugins.tallyLight.TallyLight
import java.util.logging.Logger

class RehobothFilter : TallyLightFilterInterface {
    private val logger = Logger.getLogger(RehobothFilter::class.java.name)

    override var isEnabled: Boolean = false
    private var ipCameraSourceName = "[camera] IP"

    override fun apply(tallyLights: List<TallyLight>) {

        val ipCameraTally = tallyLights.find { it.cameraSourceName == "[camera] IP" }
        if (ipCameraTally == null) {
            logger.warning("Could not find IP Camera Tally")
            return
        }

        val nonIpCameraTallies = tallyLights.filter { it != ipCameraTally }
        val isNonIpCameraTallyLive = nonIpCameraTallies.find { it.isLive } != null

        ipCameraTally.isLive = !isNonIpCameraTallyLive
        ipCameraTally.update()
    }

    override fun loadConfig(filterSettings: Map<String, Any>) {
        super.loadConfig(filterSettings)
        ipCameraSourceName = filterSettings["ipCameraSourceName"] as String
    }

    override fun saveConfig(): HashMap<String, Any> {
        val map = super.saveConfig()
        map["ipCameraSourceName"] = ipCameraSourceName
        return map
    }

    override fun toString(): String {
        return "RehobothFilter(isEnabled=$isEnabled, ipCameraSourceName=$ipCameraSourceName)"
    }
}