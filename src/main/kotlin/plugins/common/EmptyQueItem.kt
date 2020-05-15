package plugins.common

class EmptyQueItem(override val name: String) : QueItem {
    override val pluginName: String = "BasePlugin"

    override fun activate() {}

    override fun deactivate() {}

    override fun toConfigString(): String = name
}