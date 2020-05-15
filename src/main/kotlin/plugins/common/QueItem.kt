package plugins.common

interface QueItem {
    val name: String
    val group: String

    fun activate()
    fun deactivate()
}