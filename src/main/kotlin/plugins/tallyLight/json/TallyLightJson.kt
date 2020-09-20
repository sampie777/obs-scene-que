package plugins.tallyLight.json


data class TallyLightJson(
    var cameraSourceName: String,
    var host: String = ""
)

data class TallyLightFilterJson(
    var className: String,
    var enabled: Boolean
)