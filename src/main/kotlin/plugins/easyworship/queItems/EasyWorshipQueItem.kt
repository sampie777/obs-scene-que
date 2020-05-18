package plugins.easyworship.queItems

import plugins.common.BasePlugin
import plugins.common.QueItem

abstract class EasyWorshipQueItem(override val plugin: BasePlugin, override val name: String) : QueItem {

    override fun toString() = name

    override fun toConfigString(): String {
        return name
    }
}