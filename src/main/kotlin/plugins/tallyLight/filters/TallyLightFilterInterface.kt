package plugins.tallyLight.filters

import plugins.tallyLight.TallyLight

interface TallyLightFilterInterface {
    var isEnabled: Boolean
    fun apply(tallyLights: List<TallyLight>)

    fun loadConfig(filterSettings: Map<String, Any>) {
        isEnabled = filterSettings["enabled"] as Boolean? ?: false
    }

    fun saveConfig(): HashMap<String, Any> {
        return hashMapOf("enabled" to isEnabled)
    }
}