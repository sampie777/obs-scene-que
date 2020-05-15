package plugins.text

import plugins.common.BasePlugin
import plugins.common.QueItem

class PlainTextQueItem(override val plugin: BasePlugin, override val name: String) : QueItem {

    override fun activate() {}

    override fun deactivate() {}

    override fun toConfigString(): String = name
}