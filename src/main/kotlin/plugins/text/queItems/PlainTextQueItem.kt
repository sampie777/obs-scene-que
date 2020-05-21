package plugins.text.queItems

import plugins.common.QueItem
import plugins.text.TextPlugin
import plugins.text.TextPluginQueItemCompanion

class PlainTextQueItem(override val plugin: TextPlugin, override val name: String) : QueItem {

    override var executeAfterPrevious = false

    companion object QueItemCompanion : TextPluginQueItemCompanion(PlainTextQueItem::class.java, "Plain text");

    override fun activate() {}

    override fun deactivate() {}

    override fun toConfigString(): String = javaClass.simpleName + plugin.configStringSeparator + name
}