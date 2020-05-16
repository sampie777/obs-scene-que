package plugins.easyworship.queItems

import GUI
import plugins.common.BasePlugin
import plugins.easyworship.EasyWorship

class EasyWorshipLogoScreenQueItem(plugin: BasePlugin) : EasyWorshipQueItem(plugin, "Toggle logo screen") {
    override fun activate() {
        EasyWorship.doLogoScreen()
        GUI.currentFrame?.toFront()
    }

    override fun deactivate() {}
}