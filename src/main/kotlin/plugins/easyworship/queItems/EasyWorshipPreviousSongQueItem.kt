package plugins.easyworship.queItems

import GUI
import plugins.common.BasePlugin
import plugins.easyworship.EasyWorship

class EasyWorshipPreviousSongQueItem(plugin: BasePlugin) : EasyWorshipQueItem(plugin, "Previous song") {
    override fun activate() {
        EasyWorship.doPreviousSong()
        GUI.currentFrame?.toFront()
    }

    override fun deactivate() {}
}