package plugins.easyworship.queItems

import GUI
import plugins.common.BasePlugin
import plugins.easyworship.EasyWorship

class EasyWorshipPreviousVerseQueItem(plugin: BasePlugin) : EasyWorshipQueItem(plugin, "Previous verse") {
    override fun activate() {
        EasyWorship.doPreviousVerse()
        GUI.currentFrame?.toFront()
    }

    override fun deactivate() {}
}