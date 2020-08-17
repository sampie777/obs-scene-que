package plugins.utility.queItems

import objects.que.QueItem
import plugins.utility.TextQueItemsCompanion
import plugins.utility.UtilityPlugin
import java.awt.Color
import javax.swing.Icon

class PlainTextQueItem(override val plugin: UtilityPlugin, override val name: String) : QueItem {

    override var executeAfterPrevious = false
    override var quickAccessColor: Color? = plugin.quickAccessColor
    override val icon: Icon? = PlainTextQueItem.icon

    companion object QueItemCompanion : TextQueItemsCompanion(PlainTextQueItem::class.java, "Plain text")

    override fun activate() {}

    override fun deactivate() {}

    override fun toConfigString(): String = javaClass.simpleName + plugin.configStringSeparator + name
}