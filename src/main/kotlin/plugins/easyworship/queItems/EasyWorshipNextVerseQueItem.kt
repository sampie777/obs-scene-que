package plugins.easyworship.queItems

import plugins.common.BasePlugin

class EasyWorshipNextVerseQueItem(plugin: BasePlugin) : EasyWorshipQueItem(plugin, "Next verse") {
    override fun activate() {
        println("Activate next verse")
    }

    override fun deactivate() {}
}