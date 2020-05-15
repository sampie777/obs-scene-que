package plugins.common

interface QueItem {
    val name: String
    val pluginName: String

    fun activate()
    fun deactivate()

    fun toConfigString(): String
}