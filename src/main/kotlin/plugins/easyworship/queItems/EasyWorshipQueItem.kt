package plugins.easyworship.queItems

import plugins.common.QueItem

abstract class EasyWorshipQueItem(override val name: String) : QueItem {
    override val group: String = "EasyWorship"

    override fun toString() = name
}