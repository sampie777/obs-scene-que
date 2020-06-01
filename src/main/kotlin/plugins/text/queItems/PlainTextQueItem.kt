package plugins.text.queItems

import objects.que.QueItem
import plugins.text.TextPlugin
import plugins.text.TextPluginQueItemCompanion
import java.awt.Color

class PlainTextQueItem(override val plugin: TextPlugin, override val name: String) : QueItem {

    override var executeAfterPrevious = false
    override var quickAccessColor: Color? = plugin.quickAccessColor

    companion object QueItemCompanion : TextPluginQueItemCompanion(PlainTextQueItem::class.java, "Plain text");

    override fun activate() {}

    override fun deactivate() {}

    override fun toConfigString(): String = javaClass.simpleName + plugin.configStringSeparator + name
}