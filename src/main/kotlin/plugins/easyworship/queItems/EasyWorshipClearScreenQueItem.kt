package plugins.easyworship.queItems

import GUI
import plugins.common.BasePlugin
import plugins.easyworship.EasyWorship

class EasyWorshipClearScreenQueItem(plugin: BasePlugin) : EasyWorshipQueItem(plugin, "Toggle clear screen") {
    override fun activate() {
        EasyWorship.doClearScreen()
        GUI.currentFrame?.toFront()
    }

    override fun deactivate() {}
}