package plugins.common

interface QueItem {
    val name: String
    val plugin: BasePlugin

    fun activate()
    fun deactivate()

    fun toConfigString(): String
}