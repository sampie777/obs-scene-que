package plugins.easyworship.queItems

import plugins.common.BasePlugin

class EasyWorshipPreviousVerseQueItem(plugin: BasePlugin) : EasyWorshipQueItem(plugin, "Previous verse") {
    override fun activate() {
        println("Activate previous verse")
    }

    override fun deactivate() {}
}