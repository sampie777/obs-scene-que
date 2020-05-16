package plugins.easyworship.queItems

import GUI
import plugins.common.BasePlugin
import plugins.easyworship.EasyWorship

class EasyWorshipNextVerseQueItem(plugin: BasePlugin) : EasyWorshipQueItem(plugin, "Next verse") {
    override fun activate() {
        EasyWorship.doNextVerse()
        GUI.currentFrame?.toFront()
    }

    override fun deactivate() {}
}