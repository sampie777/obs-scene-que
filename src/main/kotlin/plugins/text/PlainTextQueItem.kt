package plugins.text

import plugins.common.QueItem

class PlainTextQueItem(override val plugin: TextPlugin, override val name: String) : QueItem {

    companion object : TextPluginCompanion(PlainTextQueItem::class.java, "Plain text");

    override fun activate() {}

    override fun deactivate() {}

    override fun toConfigString(): String = javaClass.simpleName + plugin.configStringSeparator + name
}